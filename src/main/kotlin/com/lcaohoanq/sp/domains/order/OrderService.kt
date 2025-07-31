package com.lcaohoanq.sp.domains.order

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.domains.cart.ICartService
import com.lcaohoanq.sp.domains.payment.IPaymentService
import com.lcaohoanq.sp.domains.product.Product
import com.lcaohoanq.sp.entities.Coupon
import com.lcaohoanq.sp.exceptions.BusinessException
import com.lcaohoanq.sp.exceptions.UnauthorizedException
import com.lcaohoanq.sp.exceptions.base.DataNotFoundException
import com.lcaohoanq.sp.extension.toOrderResponse
import com.lcaohoanq.sp.extension.toOrderSummaryResponse
import com.lcaohoanq.sp.metadata.PaginationMeta
import com.lcaohoanq.sp.repositories.*
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val productRepository: ProductRepository,
    private val cartService: ICartService,
    private val paymentService: IPaymentService,
    private val addressRepository: AddressRepository,
    private val shippingMethodRepository: ShippingMethodRepository,
    private val couponRepository: CouponRepository
) : IOrderService {

    private val log = KotlinLogging.logger {}

    override fun createOrder(userId: Long, request: OrderPort.CreateOrderRequest): OrderPort.OrderResponse {
        log.info { "Creating order for user: $userId" }
        
        // Validate address belongs to user
        val address = addressRepository.findById(request.addressId)
            .orElseThrow { DataNotFoundException("Address not found") }
        
        if (address.user.id != userId) {
            throw UnauthorizedException("Address does not belong to user")
        }
        
        // Create order
        val order = Order(
            userId = userId,
            orderNumber = generateOrderNumber(),
            addressId = request.addressId,
            shippingMethodId = request.shippingMethodId,
            couponId = request.couponId,
            notes = request.notes
        )
        
        // Process order items
        var subtotal = 0.0
        request.items.forEach { itemRequest ->
            val product = productRepository.findById(itemRequest.productId)
                .orElseThrow { DataNotFoundException("Product ${itemRequest.productId} not found") }
            
            if (!productRepository.isProductAvailable(itemRequest.productId)) {
                throw BusinessException("Product ${product.name} is not available")
            }
            
            if (product.stock < itemRequest.quantity) {
                throw BusinessException("Insufficient stock for ${product.name}. Available: ${product.stock}")
            }
            
            val orderItem = OrderItem(
                orderId = 0L, // Will be set after order is saved
                productId = itemRequest.productId,
                productName = product.name,
                productImage = product.imageUrl,
                quantity = itemRequest.quantity,
                unitPrice = product.price,
                totalPrice = product.price * itemRequest.quantity
            )
            
            order.addItem(orderItem)
            subtotal += orderItem.calculateTotalPrice()
            
            // Update product stock
            product.stock -= itemRequest.quantity
            if (product.stock == 0) {
                product.status = Product.ProductStatus.OUT_OF_STOCK
            }
            productRepository.save(product)
        }
        
        // Apply shipping
        request.shippingMethodId?.let { shippingId ->
            val shippingMethod = shippingMethodRepository.findById(shippingId)
                .orElseThrow { DataNotFoundException("Shipping method not found") }
            order.applyShipping(shippingId, shippingMethod.cost ?: 0.0, shippingMethod.estimatedDays)
        }
        
        // Apply coupon
        request.couponId?.let { couponId ->
            val coupon = couponRepository.findById(couponId)
                .orElseThrow { DataNotFoundException("Coupon not found") }
            
            val discountAmount = calculateCouponDiscount(subtotal, coupon)
            order.applyCoupon(couponId, discountAmount)
        }
        
        val savedOrder = orderRepository.save(order)
        
        // Update order items with correct order ID
        savedOrder.items.forEach { item ->
            item.orderId = savedOrder.id!!
            orderItemRepository.save(item)
        }
        
        log.info { "Order created successfully: ${savedOrder.orderNumber}" }
        return savedOrder.toOrderResponse()
    }

    override fun checkoutFromCart(userId: Long, request: OrderPort.CheckoutRequest): OrderPort.CheckoutResponse {
        log.info { "Processing checkout from cart for user: $userId" }
        
        // Get cart items
        val cart = cartService.getCart(userId)
            ?: throw BusinessException("Cart is empty")
        
        if (cart.isEmpty) {
            throw BusinessException("Cart is empty")
        }
        
        // Validate cart items
        val validation = cartService.validateCartItems(userId)
        if (validation.hasUnavailableItems) {
            throw BusinessException("Cart contains unavailable items. Please remove them first.")
        }
        
        // Convert cart items to order items
        val orderItems = if (request.useCartItems) {
            cart.items.map { cartItem ->
                OrderPort.OrderItemRequest(
                    productId = cartItem.productId,
                    quantity = cartItem.quantity
                )
            }
        } else {
            // Use selected items only
            request.selectedCartItems?.let { selectedIds ->
                cart.items.filter { it.productId in selectedIds }.map { cartItem ->
                    OrderPort.OrderItemRequest(
                        productId = cartItem.productId,
                        quantity = cartItem.quantity
                    )
                }
            } ?: throw BusinessException("No items selected for checkout")
        }
        
        // Create order request
        val orderRequest = OrderPort.CreateOrderRequest(
            addressId = request.addressId,
            shippingMethodId = request.shippingMethodId,
            couponId = request.couponId,
            notes = request.notes,
            items = orderItems
        )
        
        // Create order
        val order = createOrder(userId, orderRequest)
        
        // Process payment
        val paymentResult = paymentService.createPayment(
            orderId = order.id,
            amount = order.totalAmount,
            paymentMethod = request.paymentMethod
        )
        
        // Clear cart after successful order creation
        if (request.useCartItems) {
            cartService.clearCart(userId)
        } else {
            // Remove only selected items
            request.selectedCartItems?.let { selectedIds ->
                cartService.removeMultipleItems(userId, selectedIds)
            }
        }
        
        log.info { "Checkout completed successfully for order: ${order.orderNumber}" }
        
        return OrderPort.CheckoutResponse(
            order = order,
            paymentUrl = paymentResult.paymentUrl,
            paymentInstructions = paymentResult.instructions
        )
    }

    @Transactional(readOnly = true)
    override fun getOrderById(orderId: Long, userId: Long): OrderPort.OrderResponse {
        log.info { "Getting order $orderId for user: $userId" }
        
        val order = orderRepository.findById(orderId)
            .orElseThrow { DataNotFoundException("Order not found") }
        
        if (!canUserAccessOrder(orderId, userId)) {
            throw UnauthorizedException("Access denied to order")
        }
        
        return order.toOrderResponse()
    }

    @Transactional(readOnly = true)
    override fun getOrderByNumber(orderNumber: String, userId: Long): OrderPort.OrderResponse {
        log.info { "Getting order by number: $orderNumber for user: $userId" }
        
        val order = orderRepository.findByOrderNumber(orderNumber)
            ?: throw DataNotFoundException("Order not found")
        
        if (order.userId != userId) {
            throw UnauthorizedException("Access denied to order")
        }
        
        return order.toOrderResponse()
    }

    @Transactional(readOnly = true)
    override fun getUserOrders(userId: Long, pageable: Pageable): PageResponse<OrderPort.OrderSummaryResponse> {
        log.info { "Getting orders for user: $userId" }
        
        val pageResult = orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
        val orderResponses = pageResult.content.map { it.toOrderSummaryResponse() }
        
        return PageResponse(
            message = "User orders retrieved successfully",
            data = orderResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    override fun updateOrderStatus(orderId: Long, request: OrderPort.UpdateOrderStatusRequest): OrderPort.OrderResponse {
        log.info { "Updating order status for order: $orderId to ${request.status}" }
        
        val order = orderRepository.findById(orderId)
            .orElseThrow { DataNotFoundException("Order not found") }
        
        order.updateStatus(request.status)
        request.notes?.let { order.notes = it }
        
        val updatedOrder = orderRepository.save(order)
        
        log.info { "Order status updated successfully" }
        return updatedOrder.toOrderResponse()
    }

    override fun cancelOrder(orderId: Long, userId: Long, reason: String?): OrderPort.OrderResponse {
        log.info { "Cancelling order: $orderId for user: $userId" }
        
        val order = orderRepository.findById(orderId)
            .orElseThrow { DataNotFoundException("Order not found") }
        
        if (order.userId != userId) {
            throw UnauthorizedException("Access denied to order")
        }
        
        if (!order.canBeCancelled()) {
            throw BusinessException("Order cannot be cancelled in current status: ${order.status}")
        }
        
        // Restore product stock
        order.items.forEach { orderItem ->
            val product = productRepository.findById(orderItem.productId).orElse(null)
            product?.let {
                it.stock += orderItem.quantity
                if (it.status == Product.ProductStatus.OUT_OF_STOCK) {
                    it.status = Product.ProductStatus.ACTIVE
                }
                productRepository.save(it)
            }
        }
        
        order.updateStatus(Order.OrderStatus.CANCELLED)
        reason?.let { order.notes = "${order.notes ?: ""}\nCancellation reason: $it" }
        
        val cancelledOrder = orderRepository.save(order)
        
        log.info { "Order cancelled successfully" }
        return cancelledOrder.toOrderResponse()
    }

    override fun generateOrderNumber(): String {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val random = (1000..9999).random()
        return "ORD-$timestamp-$random"
    }

    override fun canUserAccessOrder(orderId: Long, userId: Long): Boolean {
        val order = orderRepository.findById(orderId).orElse(null)
        return order?.userId == userId
    }

    override fun getOrderStats(): OrderPort.OrderStatsResponse {
        log.info { "Getting order statistics" }
        
        val totalOrders = orderRepository.count()
        val pendingOrders = orderRepository.countByStatus(Order.OrderStatus.PENDING)
        val completedOrders = orderRepository.countByStatus(Order.OrderStatus.DELIVERED)
        val cancelledOrders = orderRepository.countByStatus(Order.OrderStatus.CANCELLED)
        val totalRevenue = orderRepository.getTotalRevenue() ?: 0.0
        val averageOrderValue = orderRepository.getAverageOrderValue() ?: 0.0
        
        return OrderPort.OrderStatsResponse(
            totalOrders = totalOrders,
            pendingOrders = pendingOrders,
            completedOrders = completedOrders,
            cancelledOrders = cancelledOrders,
            totalRevenue = totalRevenue,
            averageOrderValue = averageOrderValue
        )
    }

    override fun createOrderFromCartItems(userId: Long, cartItemIds: List<Long>, request: OrderPort.CreateOrderRequest): OrderPort.OrderResponse {
        log.info { "Creating order from specific cart items for user: $userId" }
        
        val cart = cartService.getCart(userId)
            ?: throw BusinessException("Cart is empty")
        
        val selectedItems = cart.items.filter { it.productId in cartItemIds }
        if (selectedItems.isEmpty()) {
            throw BusinessException("No valid cart items found for order creation")
        }
        
        val orderItems = selectedItems.map { cartItem ->
            OrderPort.OrderItemRequest(
                productId = cartItem.productId,
                quantity = cartItem.quantity
            )
        }
        
        val orderRequest = request.copy(items = orderItems)
        val order = createOrder(userId, orderRequest)
        
        // Remove selected items from cart
        cartService.removeMultipleItems(userId, cartItemIds)
        
        return order
    }

    @Transactional(readOnly = true)
    override fun getUserOrdersByStatus(userId: Long, status: Order.OrderStatus, pageable: Pageable): PageResponse<OrderPort.OrderSummaryResponse> {
        log.info { "Getting orders for user: $userId with status: $status" }
        
        val pageResult = orderRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status, pageable)
        val orderResponses = pageResult.content.map { it.toOrderSummaryResponse() }
        
        return PageResponse(
            message = "User orders by status retrieved successfully",
            data = orderResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getAllOrders(pageable: Pageable): PageResponse<OrderPort.OrderResponse> {
        log.info { "Getting all orders (admin)" }
        
        val pageResult = orderRepository.findAllByOrderByCreatedAtDesc(pageable)
        val orderResponses = pageResult.content.map { it.toOrderResponse() }
        
        return PageResponse(
            message = "All orders retrieved successfully",
            data = orderResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    override fun confirmOrder(orderId: Long): OrderPort.OrderResponse {
        log.info { "Confirming order: $orderId" }
        
        val order = orderRepository.findById(orderId)
            .orElseThrow { DataNotFoundException("Order not found") }
        
        if (order.status != Order.OrderStatus.PENDING) {
            throw BusinessException("Order cannot be confirmed. Current status: ${order.status}")
        }
        
        order.updateStatus(Order.OrderStatus.CONFIRMED)
        val confirmedOrder = orderRepository.save(order)
        
        log.info { "Order confirmed successfully" }
        return confirmedOrder.toOrderResponse()
    }

    override fun shipOrder(orderId: Long, trackingNumber: String?): OrderPort.OrderResponse {
        log.info { "Shipping order: $orderId" }
        
        val order = orderRepository.findById(orderId)
            .orElseThrow { DataNotFoundException("Order not found") }
        
        if (order.status != Order.OrderStatus.CONFIRMED) {
            throw BusinessException("Order cannot be shipped. Current status: ${order.status}")
        }
        
        order.updateStatus(Order.OrderStatus.SHIPPED)
        trackingNumber?.let { 
            order.notes = "${order.notes ?: ""}\nTracking Number: $it"
        }
        
        val shippedOrder = orderRepository.save(order)
        
        log.info { "Order shipped successfully" }
        return shippedOrder.toOrderResponse()
    }

    override fun deliverOrder(orderId: Long): OrderPort.OrderResponse {
        log.info { "Delivering order: $orderId" }
        
        val order = orderRepository.findById(orderId)
            .orElseThrow { DataNotFoundException("Order not found") }
        
        if (order.status != Order.OrderStatus.SHIPPED) {
            throw BusinessException("Order cannot be delivered. Current status: ${order.status}")
        }
        
        order.updateStatus(Order.OrderStatus.DELIVERED)
        val deliveredOrder = orderRepository.save(order)
        
        log.info { "Order delivered successfully" }
        return deliveredOrder.toOrderResponse()
    }

    @Transactional(readOnly = true)
    override fun searchOrders(request: OrderPort.OrderSearchRequest, pageable: Pageable): PageResponse<OrderPort.OrderResponse> {
        log.info { "Searching orders with criteria: $request" }
        
        val pageResult = orderRepository.findOrdersWithCriteria(
            orderNumber = request.orderNumber,
            status = request.status,
            userId = request.userId,
            startDate = request.fromDate,
            endDate = request.toDate,
            minAmount = request.minAmount,
            maxAmount = request.maxAmount,
            pageable = pageable
        )
        
        val orderResponses = pageResult.content.map { it.toOrderResponse() }
        
        return PageResponse(
            message = "Orders search completed successfully",
            data = orderResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun searchUserOrders(userId: Long, request: OrderPort.OrderSearchRequest, pageable: Pageable): PageResponse<OrderPort.OrderSummaryResponse> {
        log.info { "Searching orders for user: $userId with criteria: $request" }
        
        val pageResult = orderRepository.findOrdersWithCriteria(
            orderNumber = request.orderNumber,
            status = request.status,
            userId = userId,
            startDate = request.fromDate,
            endDate = request.toDate,
            minAmount = request.minAmount,
            maxAmount = request.maxAmount,
            pageable = pageable
        )
        
        val orderResponses = pageResult.content.map { it.toOrderSummaryResponse() }
        
        return PageResponse(
            message = "User orders search completed successfully",
            data = orderResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getUserOrderStats(userId: Long): OrderPort.OrderStatsResponse {
        log.info { "Getting order statistics for user: $userId" }
        
        val totalOrders = orderRepository.countByUserId(userId)
        val pendingOrders = orderRepository.countByUserIdAndStatus(userId, Order.OrderStatus.PENDING)
        val completedOrders = orderRepository.countByUserIdAndStatus(userId, Order.OrderStatus.DELIVERED)
        val cancelledOrders = orderRepository.countByUserIdAndStatus(userId, Order.OrderStatus.CANCELLED)
        val totalSpent = orderRepository.getTotalSpentByUser(userId) ?: 0.0
        val averageOrderValue = if (totalOrders > 0) totalSpent / totalOrders else 0.0
        
        return OrderPort.OrderStatsResponse(
            totalOrders = totalOrders,
            pendingOrders = pendingOrders,
            completedOrders = completedOrders,
            cancelledOrders = cancelledOrders,
            totalRevenue = totalSpent,
            averageOrderValue = averageOrderValue
        )
    }

    @Transactional(readOnly = true)
    override fun validateOrder(orderId: Long): Boolean {
        val order = orderRepository.findById(orderId).orElse(null) ?: return false
        
        // Validate all order items still exist and have sufficient stock
        return order.items.all { orderItem ->
            val product = productRepository.findById(orderItem.productId).orElse(null)
            product != null && productRepository.isProductAvailable(orderItem.productId)
        }
    }

    override fun calculateOrderTotals(items: List<OrderPort.OrderItemRequest>, shippingFee: Double, discountAmount: Double): Double {
        val subtotal = items.sumOf { item ->
            val product = productRepository.findById(item.productId).orElse(null)
            product?.let { it.price * item.quantity } ?: 0.0
        }
        
        return subtotal + shippingFee - discountAmount
    }

    @Transactional(readOnly = true)
    override fun getRecentOrders(pageable: Pageable): PageResponse<OrderPort.OrderResponse> {
        log.info { "Getting recent orders" }
        
        val pageResult = orderRepository.findRecentOrders(pageable)
        val orderResponses = pageResult.content.map { it.toOrderResponse() }
        
        return PageResponse(
            message = "Recent orders retrieved successfully",
            data = orderResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getPendingOrders(pageable: Pageable): PageResponse<OrderPort.OrderResponse> {
        log.info { "Getting pending orders" }
        
        val pageResult = orderRepository.findByStatusOrderByCreatedAtDesc(Order.OrderStatus.PENDING, pageable)
        val orderResponses = pageResult.content.map { it.toOrderResponse() }
        
        return PageResponse(
            message = "Pending orders retrieved successfully",
            data = orderResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getOverdueOrders(): List<OrderPort.OrderResponse> {
        log.info { "Getting overdue orders" }
        
        val cutoffDate = LocalDateTime.now().minusDays(7) // Orders older than 7 days and still pending
        val overdueOrders = orderRepository.findOverdueOrders(Order.OrderStatus.PENDING, cutoffDate)
        
        return overdueOrders.map { it.toOrderResponse() }
    }

    private fun calculateCouponDiscount(subtotal: Double, coupon: Coupon): Double {
        return coupon.discountPercent?.let { percent ->
            subtotal * (percent / 100.0)
        } ?: 0.0
    }
}
