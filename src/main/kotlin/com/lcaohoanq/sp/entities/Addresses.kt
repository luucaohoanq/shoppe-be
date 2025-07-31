package com.lcaohoanq.sp.entities

import com.lcaohoanq.sp.bases.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "addresses")
class Addresses(): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Int? = null

    @Column(name = "user_id", nullable = false)
    var userId: Int = 0

    @Column(name = "recipient_name")
    var recipientName: String = ""
    var phone: String = ""

    @Column(name = "address_line")
    var addressLine: String = ""
    var city: String = ""
    var district: String = ""

    @Column(name = "postal_code")
    var postalCode: String = ""

    @Column(name = "is_default")
    var isDefault: Boolean = false

}