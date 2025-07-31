package com.lcaohoanq.sp.domains.order

import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.entities.Addresses
import com.lcaohoanq.sp.entities.Coupon
import com.lcaohoanq.sp.entities.ShippingMethod
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "order_number", unique = true, nullable = false)
    var orderNumber: String,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.PENDING,

    @Column(name = "address_id")
    var addressId: Long? = null,

    @Column(name = "coupon_id")
    var couponId: Long? = null,

    @Column(name = "shipping_method_id")
    var shippingMethodId: Long? = null,

    @Column(name = "subtotal", nullable = false)
    var subtotal: Double = 0.0,

    @Column(name = "discount_amount")
    var discountAmount: Double = 0.0,

    @Column(name = "shipping_fee")
    var shippingFee: Double = 0.0,

    @Column(name = "tax_amount")
    var taxAmount: Double = 0.0,

    @Column(name = "total_amount", nullable = false)
    var totalAmount: Double = 0.0,

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    var paymentStatus: PaymentStatus = PaymentStatus.PENDING,

    @Column(name = "shipping_status")
    @Enumerated(EnumType.STRING)
    var shippingStatus: ShippingStatus = ShippingStatus.NOT_SHIPPED,

    @Column(name = "estimated_delivery")
    var estimatedDelivery: LocalDateTime? = null,

    @Column(name = "delivered_at")
    var deliveredAt: LocalDateTime? = null,

    @Column(name = "notes", columnDefinition = "TEXT")
    var notes: String? = null,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var items: MutableList<OrderItem> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", insertable = false, updatable = false)
    var address: Addresses? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    var coupon: Coupon? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_method_id", insertable = false, updatable = false)
    var shippingMethod: ShippingMethod? = null

) : BaseEntity() {

    enum class OrderStatus {
        PENDING,
        CONFIRMED,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED,
        REFUNDED,
        RETURNED
    }

    enum class PaymentStatus {
        PENDING,
        PAID,
        FAILED,
        REFUNDED,
        PARTIAL_REFUND
    }

    enum class ShippingStatus {
        NOT_SHIPPED,
        PREPARING,
        SHIPPED,
        IN_TRANSIT,
        OUT_FOR_DELIVERY,
        DELIVERED,
        RETURNED
    }

    fun calculateTotal(): Double {
        return subtotal + shippingFee + taxAmount - discountAmount
    }

    fun calculateSubtotal(): Double {
        return items.sumOf { it.calculateTotalPrice() }
    }

    fun addItem(item: OrderItem) {
        item.order = this
        items.add(item)
        recalculateTotal()
    }

    fun removeItem(item: OrderItem) {
        items.remove(item)
        recalculateTotal()
    }

    fun recalculateTotal() {
        subtotal = items.sumOf { it.calculateTotalPrice() }
        totalAmount = subtotal + shippingFee + taxAmount - discountAmount
    }

    fun canBeCancelled(): Boolean {
        return status in listOf(OrderStatus.PENDING, OrderStatus.CONFIRMED)
    }

    fun canBeModified(): Boolean {
        return status == OrderStatus.PENDING
    }

    fun isCompleted(): Boolean {
        return status == OrderStatus.DELIVERED
    }

    fun isCancelled(): Boolean {
        return status in listOf(OrderStatus.CANCELLED, OrderStatus.REFUNDED, OrderStatus.RETURNED)
    }

    fun updateStatus(newStatus: OrderStatus) {
        when (newStatus) {
            OrderStatus.DELIVERED -> deliveredAt = LocalDateTime.now()
            OrderStatus.CANCELLED -> {
                if (!canBeCancelled()) {
                    throw IllegalStateException("Order cannot be cancelled in current status: $status")
                }
            }
            else -> { /* No special handling needed */ }
        }
        status = newStatus
    }

    fun applyShipping(shippingMethodId: Long, fee: Double, estimatedDays: Int) {
        this.shippingMethodId = shippingMethodId
        this.shippingFee = fee
        this.estimatedDelivery = LocalDateTime.now().plusDays(estimatedDays.toLong())
        recalculateTotal()
    }

    fun applyCoupon(couponId: Long, discountAmount: Double) {
        this.couponId = couponId
        this.discountAmount = discountAmount
        recalculateTotal()
    }
}
