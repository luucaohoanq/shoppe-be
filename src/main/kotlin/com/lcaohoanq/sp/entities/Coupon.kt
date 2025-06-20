package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.util.Date

@Entity
@Table(name = "coupons")
class Coupon(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "coupons_seq", sequenceName = "coupons_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupons_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var code: String? = null
    var discountPercent: Double? = null
    var validFrom: Date? = null
    var validTo: Date? = null
    var usageLimit: Double? = null

}