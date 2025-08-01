package com.lcaohoanq.sp.domains.order

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.utils.SecurityUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "orders", description = "APIs for order operations")
class OrderController(
    private val orderService: IOrderService
) : BaseController() {

    private val log = KotlinLogging.logger {}

    @PostMapping
    @Operation(
        summary = "Create a new order",
        description = "Create a new order with the specified items"
    )
    fun createOrder(
        @Valid @RequestBody request: OrderPort.CreateOrderRequest
    ): ResponseEntity<MyApiResponse<OrderPort.OrderResponse>> {
        log.info { "Creating order for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val order = orderService.createOrder(userId, request)

        return created(order)
    }

    @PostMapping("/checkout")
    @Operation(
        summary = "Checkout from cart",
        description = "Create order from cart items and process payment"
    )
    fun checkoutFromCart(
        @Valid @RequestBody request: OrderPort.CheckoutRequest
    ): ResponseEntity<MyApiResponse<OrderPort.CheckoutResponse>> {
        log.info { "Processing checkout for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val checkoutResponse = orderService.checkoutFromCart(userId, request)

        return ok("Checkout completed successfully", checkoutResponse)
    }

    @PostMapping("/from-cart")
    @Operation(
        summary = "Create order from specific cart items",
        description = "Create order from selected cart items"
    )
    fun createOrderFromCartItems(
        @RequestParam cartItemIds: List<Long>,
        @Valid @RequestBody request: OrderPort.CreateOrderRequest
    ): ResponseEntity<MyApiResponse<OrderPort.OrderResponse>> {
        log.info { "Creating order from cart items for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val order = orderService.createOrderFromCartItems(userId, cartItemIds, request)

        return created(order)
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Retrieve order details by order ID")
    fun getOrderById(
        @Parameter(description = "Order ID") @PathVariable orderId: Long
    ): ResponseEntity<MyApiResponse<OrderPort.OrderResponse>> {
        log.info { "Getting order: $orderId for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val order = orderService.getOrderById(orderId, userId)

        return ok("Order retrieved successfully", order)
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(
        summary = "Get order by order number",
        description = "Retrieve order details by order number"
    )
    fun getOrderByNumber(
        @Parameter(description = "Order number") @PathVariable orderNumber: String
    ): ResponseEntity<MyApiResponse<OrderPort.OrderResponse>> {
        log.info { "Getting order by number: $orderNumber for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val order = orderService.getOrderByNumber(orderNumber, userId)

        return ok("Order retrieved successfully", order)
    }

    @GetMapping("/my-orders")
    @Operation(
        summary = "Get user orders",
        description = "Retrieve paginated list of user's orders"
    )
    fun getUserOrders(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "desc") sortDirection: String
    ): ResponseEntity<PageResponse<OrderPort.OrderSummaryResponse>> {
        log.info { "Getting orders for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val sort = if (sortDirection.lowercase() == "desc") {
            Sort.by(sortBy).descending()
        } else {
            Sort.by(sortBy).ascending()
        }
        val pageable = PageRequest.of(page, size, sort)

        val orders = orderService.getUserOrders(userId, pageable)

        return ResponseEntity.ok(orders)
    }

    @GetMapping("/my-orders/status/{status}")
    @Operation(
        summary = "Get user orders by status",
        description = "Retrieve user's orders filtered by status"
    )
    fun getUserOrdersByStatus(
        @Parameter(description = "Order status") @PathVariable status: Order.OrderStatus,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PageResponse<OrderPort.OrderSummaryResponse>> {
        log.info { "Getting orders with status $status for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())

        val orders = orderService.getUserOrdersByStatus(userId, status, pageable)

        return ResponseEntity.ok(orders)
    }

    @PostMapping("/search/my-orders")
    @Operation(summary = "Search user orders", description = "Search user's orders with filters")
    fun searchUserOrders(
        @Valid @RequestBody request: OrderPort.OrderSearchRequest,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PageResponse<OrderPort.OrderSummaryResponse>> {
        log.info { "Searching orders for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())

        val orders = orderService.searchUserOrders(userId, request, pageable)

        return ResponseEntity.ok(orders)
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel an existing order")
    fun cancelOrder(
        @Parameter(description = "Order ID") @PathVariable orderId: Long,
        @RequestParam(required = false) reason: String?
    ): ResponseEntity<MyApiResponse<OrderPort.OrderResponse>> {
        log.info { "Cancelling order: $orderId for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val order = orderService.cancelOrder(orderId, userId, reason)

        return ok("Order cancelled successfully", order)
    }

    @GetMapping("/my-stats")
    @Operation(
        summary = "Get user order statistics",
        description = "Retrieve user's order statistics"
    )
    fun getUserOrderStats(): ResponseEntity<MyApiResponse<OrderPort.OrderStatsResponse>> {
        log.info { "Getting order stats for user: ${SecurityUtils.getCurrentUserId()}" }

        val userId = SecurityUtils.getCurrentUserId()
        val stats = orderService.getUserOrderStats(userId)

        return ok("User order statistics retrieved successfully", stats)
    }

    @GetMapping("/{orderId}/validate")
    @Operation(summary = "Validate order", description = "Validate if order is still valid")
    fun validateOrder(
        @Parameter(description = "Order ID") @PathVariable orderId: Long
    ): ResponseEntity<MyApiResponse<Boolean>> {
        log.info { "Validating order: $orderId" }

        val isValid = orderService.validateOrder(orderId)

        return ok("Order validation completed", isValid)
    }

    // Admin endpoints
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Get all orders (Admin)",
        description = "Retrieve all orders for admin/manager"
    )
    fun getAllOrders(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PageResponse<OrderPort.OrderResponse>> {
        log.info { "Getting all orders (admin)" }

        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        val orders = orderService.getAllOrders(pageable)

        return ResponseEntity.ok(orders)
    }

    @PostMapping("/admin/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Search orders (Admin)",
        description = "Search orders with filters for admin/manager"
    )
    fun searchOrders(
        @Valid @RequestBody request: OrderPort.OrderSearchRequest,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PageResponse<OrderPort.OrderResponse>> {
        log.info { "Searching orders (admin)" }

        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        val orders = orderService.searchOrders(request, pageable)

        return ResponseEntity.ok(orders)
    }

    @PutMapping("/admin/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Update order status (Admin)",
        description = "Update order status for admin/manager"
    )
    fun updateOrderStatus(
        @Parameter(description = "Order ID") @PathVariable orderId: Long,
        @Valid @RequestBody request: OrderPort.UpdateOrderStatusRequest
    ): ResponseEntity<MyApiResponse<OrderPort.OrderResponse>> {
        log.info { "Updating order status for order: $orderId to ${request.status}" }

        val order = orderService.updateOrderStatus(orderId, request)

        return MyApiResponse.success(data = order)
    }

    @PutMapping("/admin/{orderId}/confirm")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Confirm order (Admin)", description = "Confirm order for admin/manager")
    fun confirmOrder(
        @Parameter(description = "Order ID") @PathVariable orderId: Long
    ): ResponseEntity<MyApiResponse<OrderPort.OrderResponse>> {
        log.info { "Confirming order: $orderId" }

        val order = orderService.confirmOrder(orderId)

        return MyApiResponse.success(data = order)
    }

    @PutMapping("/admin/{orderId}/ship")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Ship order (Admin)",
        description = "Mark order as shipped for admin/manager"
    )
    fun shipOrder(
        @Parameter(description = "Order ID") @PathVariable orderId: Long,
        @RequestParam(required = false) trackingNumber: String?
    ): ResponseEntity<MyApiResponse<OrderPort.OrderResponse>> {
        log.info { "Shipping order: $orderId" }

        val order = orderService.shipOrder(orderId, trackingNumber)

        return MyApiResponse.success(data = order)

    }

    @PutMapping("/admin/{orderId}/deliver")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Deliver order (Admin)",
        description = "Mark order as delivered for admin/manager"
    )
    fun deliverOrder(
        @Parameter(description = "Order ID") @PathVariable orderId: Long
    ): ResponseEntity<MyApiResponse<OrderPort.OrderResponse>> {
        log.info { "Delivering order: $orderId" }

        val order = orderService.deliverOrder(orderId)

        return MyApiResponse.success(data = order)
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Get order statistics (Admin)",
        description = "Retrieve order statistics for admin/manager"
    )
    fun getOrderStats(): ResponseEntity<MyApiResponse<OrderPort.OrderStatsResponse>> {
        log.info { "Getting order statistics (admin)" }

        val stats = orderService.getOrderStats()

        return MyApiResponse.success(
            data = stats
        )
    }

    @GetMapping("/admin/recent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Get recent orders (Admin)",
        description = "Retrieve recent orders for admin/manager"
    )
    fun getRecentOrders(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PageResponse<OrderPort.OrderResponse>> {
        log.info { "Getting recent orders (admin)" }

        val pageable = PageRequest.of(page, size)
        val orders = orderService.getRecentOrders(pageable)

        return ResponseEntity.ok(orders)
    }

    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Get pending orders (Admin)",
        description = "Retrieve pending orders for admin/manager"
    )
    fun getPendingOrders(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PageResponse<OrderPort.OrderResponse>> {
        log.info { "Getting pending orders (admin)" }

        val pageable = PageRequest.of(page, size)
        val orders = orderService.getPendingOrders(pageable)

        return ResponseEntity.ok(orders)
    }

    @GetMapping("/admin/overdue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Get overdue orders (Admin)",
        description = "Retrieve overdue orders for admin/manager"
    )
    fun getOverdueOrders(): ResponseEntity<MyApiResponse<List<OrderPort.OrderResponse>>> {
        log.info { "Getting overdue orders (admin)" }

        val orders = orderService.getOverdueOrders()

        return MyApiResponse.success(
            data = orders
        )
    }
}
