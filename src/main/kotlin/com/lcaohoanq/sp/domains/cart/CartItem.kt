package com.lcaohoanq.sp.domains.cart

import com.lcaohoanq.sp.domains.product.Product
import jakarta.persistence.*

@Entity
@Table(name = "cart_items")
class CartItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "cart_id", nullable = false)
    val cartId: Long,

    @Column(name = "product_id", nullable = false)
    val productId: Long,

    @Column(name = "quantity", nullable = false)
    var quantity: Int,

    @Column(name = "price_at_time", nullable = false)
    var priceAtTime: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", insertable = false, updatable = false)
    var cart: Cart? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    var product: Product? = null

) {
    constructor() : this(null, 0L, 0L, 0, 0.0)

    fun getTotalPrice(): Double {
        return priceAtTime * quantity
    }

    fun updateQuantity(newQuantity: Int) {
        require(newQuantity > 0) { "Quantity must be positive" }
        this.quantity = newQuantity
    }
}
