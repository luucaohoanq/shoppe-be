package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
class Order(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "orders_seq", sequenceName = "orders_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var userId: Int? = null
    var addressId: Int? = null
    var status: Int? = null
    var couponId: Int? = null
    var shippingMethodId: Int? = null
    var totalAmount: Double? = null

}