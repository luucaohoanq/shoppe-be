package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_reviews")
class ProductReview(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "product_reviews_seq", sequenceName = "product_reviews_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_reviews_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var productId: Int = 0
    @Column(name = "user_id", nullable = false)
    var userId: Int = 0
    var rating: Int = 0
    var comment: String? = null

}