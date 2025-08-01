package com.lcaohoanq.sp.entities

import com.lcaohoanq.sp.bases.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_reviews")
class ProductReview(): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Int? = null

    var productId: Int = 0
    @Column(name = "user_id", nullable = false)
    var userId: Int = 0
    var rating: Int = 0
    var comment: String? = null

}