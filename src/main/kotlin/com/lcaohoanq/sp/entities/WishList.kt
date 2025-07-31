package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "wishlists")
class WishList(): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    var userId: Int? = null
    var productId: Int? = null

}