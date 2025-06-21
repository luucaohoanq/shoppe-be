package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

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