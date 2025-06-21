package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "addresses")
class Addresses(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "addresses_seq", sequenceName = "addresses_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addresses_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

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