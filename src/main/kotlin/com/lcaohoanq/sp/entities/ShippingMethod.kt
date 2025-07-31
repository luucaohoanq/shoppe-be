package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "shipping_methods")
class ShippingMethod(): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    var name: String = ""
    var cost: Double? = null
    var estimatedDays: Int = 0
    var active: Boolean = true

}