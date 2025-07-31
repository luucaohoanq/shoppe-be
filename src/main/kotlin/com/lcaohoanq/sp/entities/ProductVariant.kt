package com.lcaohoanq.sp.entities

import com.lcaohoanq.sp.bases.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_variants")
class ProductVariant(): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    var productId: Int = 0
    var variantName: String = ""
    var priceAdjustment: String = ""
    var stock: Int = 0

}