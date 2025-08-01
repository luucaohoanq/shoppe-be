package com.lcaohoanq.sp.domains.order

import com.lcaohoanq.sp.apis.PageResponse
import org.springframework.data.domain.Pageable

interface IOrderService {
    
    // Order creation and checkout
    fun createOrder(userId: Long, request: OrderPort.CreateOrderRequest): OrderPort.OrderResponse
    fun checkoutFromCart(userId: Long, request: OrderPort.CheckoutRequest): OrderPort.CheckoutResponse
    fun createOrderFromCartItems(userId: Long, cartItemIds: List<Long>, request: OrderPort.CreateOrderRequest): OrderPort.OrderResponse
    
    // Order retrieval
    fun getOrderById(orderId: Long, userId: Long): OrderPort.OrderResponse
    fun getOrderByNumber(orderNumber: String, userId: Long): OrderPort.OrderResponse
    fun getUserOrders(userId: Long, pageable: Pageable): PageResponse<OrderPort.OrderSummaryResponse>
    fun getUserOrdersByStatus(userId: Long, status: Order.OrderStatus, pageable: Pageable): PageResponse<OrderPort.OrderSummaryResponse>
    fun getAllOrders(pageable: Pageable): PageResponse<OrderPort.OrderResponse>
    
    // Order management
    fun updateOrderStatus(orderId: Long, request: OrderPort.UpdateOrderStatusRequest): OrderPort.OrderResponse
    fun cancelOrder(orderId: Long, userId: Long, reason: String?): OrderPort.OrderResponse
    fun confirmOrder(orderId: Long): OrderPort.OrderResponse
    fun shipOrder(orderId: Long, trackingNumber: String?): OrderPort.OrderResponse
    fun deliverOrder(orderId: Long): OrderPort.OrderResponse
    
    // Order search and filtering
    fun searchOrders(request: OrderPort.OrderSearchRequest, pageable: Pageable): PageResponse<OrderPort.OrderResponse>
    fun searchUserOrders(userId: Long, request: OrderPort.OrderSearchRequest, pageable: Pageable): PageResponse<OrderPort.OrderSummaryResponse>
    
    // Order analytics
    fun getOrderStats(): OrderPort.OrderStatsResponse
    fun getUserOrderStats(userId: Long): OrderPort.OrderStatsResponse
    
    // Order validation and utilities
    fun validateOrder(orderId: Long): Boolean
    fun canUserAccessOrder(orderId: Long, userId: Long): Boolean
    fun generateOrderNumber(): String
    fun calculateOrderTotals(items: List<OrderPort.OrderItemRequest>, shippingFee: Double, discountAmount: Double): Double
    
    // Admin operations
    fun getRecentOrders(pageable: Pageable): PageResponse<OrderPort.OrderResponse>
    fun getPendingOrders(pageable: Pageable): PageResponse<OrderPort.OrderResponse>
    fun getOverdueOrders(): List<OrderPort.OrderResponse>
}
