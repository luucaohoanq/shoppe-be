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
@Table(name = "shipping_methods")
class ShippingMethod(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "shipping_methods_seq", sequenceName = "shipping_methods_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipping_methods_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var name: String = ""
    var cost: Double? = null
    var estimatedDays: Int = 0

}