package com.lcaohoanq.sp.entities

import jakarta.persistence.Entity
import jakarta.persistence.Table
import BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator

@Entity
@Table(name = "wishlists")
class WishList(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "wishlists_seq", sequenceName = "wishlists_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wishlists_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var userId: Int? = null
    var productId: Int? = null

}