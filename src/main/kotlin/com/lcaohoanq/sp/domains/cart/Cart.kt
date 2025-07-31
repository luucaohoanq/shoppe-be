package com.lcaohoanq.sp.domains.cart

import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.domains.user.User
import jakarta.persistence.*

@Entity
@Table(name = "carts")
class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var items: MutableList<CartItem> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    var user: User? = null

) : BaseEntity() {

    fun addItem(item: CartItem) {
        // Check if item already exists
        val existingItem = items.find { it.productId == item.productId }
        if (existingItem != null) {
            existingItem.quantity += item.quantity
        } else {
            item.cart = this
            items.add(item)
        }
    }

    fun removeItem(productId: Long) {
        items.removeIf { it.productId == productId }
    }

    fun updateItemQuantity(productId: Long, quantity: Int) {
        val item = items.find { it.productId == productId }
        if (item != null) {
            if (quantity <= 0) {
                removeItem(productId)
            } else {
                item.quantity = quantity
            }
        }
    }

    fun clear() {
        items.clear()
    }

    fun getTotalAmount(): Double {
        return items.sumOf { it.getTotalPrice() }
    }

    fun getTotalItems(): Int {
        return items.sumOf { it.quantity }
    }

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }
}
