package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "coupons")
class Coupon(): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    var code: String? = null
    var discountPercent: Double? = null
    var validFrom: Date? = null
    var validTo: Date? = null
    var usageLimit: Double? = null
    var active: Boolean = true
    var expiryDate: Date? = null

}