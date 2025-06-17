package com.lcaohoanq.sp.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "cart_items")
class CartItem {

    @Id
    @SequenceGenerator(name = "cart_items_seq", sequenceName = "cart_items_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_items_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var cartId: Int? = null
    var productId: Int? = null
    var quantity: Int? = null

}