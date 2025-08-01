package com.lcaohoanq.sp.domains.order

import com.lcaohoanq.sp.domains.payment.Payment
import jakarta.validation.constraints.*
import java.sql.Timestamp
import java.time.LocalDateTime

object OrderPort {

    data class CreateOrderRequest(
        @field:NotNull(message = "Address ID is required")
        @field:Positive(message = "Address ID must be positive")
        val addressId: Long,

        @field:Positive(message = "Shipping method ID must be positive")
        val shippingMethodId: Long? = null,

        @field:Positive(message = "Coupon ID must be positive")
        val couponId: Long? = null,

        @field:Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        val notes: String? = null,

        @field:NotEmpty(message = "Order items cannot be empty")
        val items: List<OrderItemRequest>
    )

    data class OrderItemRequest(
        @field:NotNull(message = "Product ID is required")
        @field:Positive(message = "Product ID must be positive")
        val productId: Long,

        @field:NotNull(message = "Quantity is required")
        @field:Min(value = 1, message = "Quantity must be at least 1")
        @field:Max(value = 999, message = "Quantity cannot exceed 999")
        val quantity: Int
    )

    data class UpdateOrderStatusRequest(
        @field:NotNull(message = "Status is required")
        val status: Order.OrderStatus,

        @field:Size(max = 500, message = "Notes cannot exceed 500 characters")
        val notes: String? = null
    )

    data class OrderResponse(
        val id: Long,
        val orderNumber: String,
        val userId: Long,
        val status: Order.OrderStatus,
        val paymentStatus: Order.PaymentStatus,
        val shippingStatus: Order.ShippingStatus,
        val subtotal: Double,
        val discountAmount: Double,
        val shippingFee: Double,
        val taxAmount: Double,
        val totalAmount: Double,
        val estimatedDelivery: LocalDateTime?,
        val deliveredAt: LocalDateTime?,
        val notes: String?,
        val items: List<OrderItemResponse>,
        val address: AddressResponse?,
        val shippingMethod: ShippingMethodResponse?,
        val couponId: Long?,
        val createdAt: Timestamp?,
        val updatedAt: Timestamp?,
        val canBeCancelled: Boolean,
        val canBeModified: Boolean,
        val isCompleted: Boolean,
        val addressId: Long? = null,
        val shippingMethodId: Long? = null,
        val shippingCost: Double? = null,
    )

    data class OrderItemResponse(
        val id: Long,
        val productId: Long,
        val productName: String,
        val productImage: String?,
        val quantity: Int,
        val unitPrice: Double,
        val totalPrice: Double
    )

    data class AddressResponse(
        val id: Long,
        val recipientName: String,
        val phone: String,
        val addressLine: String,
        val city: String,
        val district: String,
        val postalCode: String
    )

    data class ShippingMethodResponse(
        val id: Long,
        val name: String,
        val cost: Double,
        val estimatedDays: Int
    )

    data class CouponResponse(
        val id: Long,
        val code: String,
        val discountPercent: Double?
    )

    data class OrderSummaryResponse(
        val id: Long,
        val orderNumber: String,
        val status: Order.OrderStatus,
        val totalAmount: Double,
        val totalItems: Int,
        val createdAt: Timestamp?
    )

    data class OrderSearchRequest(
        val orderNumber: String,
        val status: Order.OrderStatus? = null,
        val paymentStatus: Order.PaymentStatus? = null,
        val shippingStatus: Order.ShippingStatus? = null,
        val fromDate: LocalDateTime? = null,
        val toDate: LocalDateTime? = null,
        val minAmount: Double? = null,
        val maxAmount: Double? = null,
        val userId: Long? = null
    )

    data class OrderStatsResponse(
        val totalOrders: Long,
        val pendingOrders: Long,
        val completedOrders: Long,
        val cancelledOrders: Long,
        val totalRevenue: Double,
        val averageOrderValue: Double
    )

    data class CheckoutRequest(
        @field:NotNull(message = "Address ID is required")
        @field:Positive(message = "Address ID must be positive")
        val addressId: Long,

        @field:Positive(message = "Shipping method ID must be positive")
        val shippingMethodId: Long? = null,

        @field:Positive(message = "Coupon ID must be positive")
        val couponId: Long? = null,

        @field:Size(max = 1000, message = "Notes cannot exceed 1000 characters")
        val notes: String? = null,

        @field:NotNull(message = "Payment method is required")
        val paymentMethod: Payment.PaymentMethod = Payment.PaymentMethod.CASH_ON_DELIVERY,

        val useCartItems: Boolean = true,

        val selectedCartItems: List<Long>? = null
    )

    data class CheckoutResponse(
        val order: OrderResponse,
        val paymentUrl: String? = null,
        val paymentInstructions: String? = null
    )
}
