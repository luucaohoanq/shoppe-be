package com.lcaohoanq.sp.entities

import jakarta.persistence.*

@Entity
@Table(name = "order_items")
class OrderItem {

    @Id
    @SequenceGenerator(name = "order_items_seq", sequenceName = "order_items_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_items_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var orderId: Int? = null
    var productId: Int? = null
    var quantity: Int? = null
    var price: Double? = null

}