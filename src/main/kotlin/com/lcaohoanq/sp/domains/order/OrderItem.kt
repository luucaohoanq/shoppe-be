package com.lcaohoanq.sp.domains.order

import com.lcaohoanq.sp.domains.product.Product
import jakarta.persistence.*

@Entity
@Table(name = "order_items")
class OrderItem(
    @Id
    @SequenceGenerator(name = "order_items_seq", sequenceName = "order_items_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_items_seq")
    @Column(name = "id", unique = true, nullable = false)
    val id: Long? = null,

    @Column(name = "order_id", nullable = false)
    var orderId: Long,

    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(name = "product_name", nullable = false)
    var productName: String,

    @Column(name = "product_image")
    var productImage: String? = null,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @Column(name = "unit_price", nullable = false)
    var unitPrice: Double,


    @Column(name = "total_price", nullable = false)
    var totalPrice: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    var order: Order? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    var product: Product? = null

) {
    constructor() : this(null, 0L, 0L, "", null, 0, 0.0, 0.0)

    fun calculateTotalPrice(): Double {
        return unitPrice * quantity
    }

    fun updateQuantity(newQuantity: Int) {
        require(newQuantity > 0) { "Quantity must be positive" }
        this.quantity = newQuantity
        this.totalPrice = calculateTotalPrice()
    }

    fun updatePrice(newUnitPrice: Double) {
        require(newUnitPrice >= 0) { "Price cannot be negative" }
        this.unitPrice = newUnitPrice
        this.totalPrice = calculateTotalPrice()
    }
}
