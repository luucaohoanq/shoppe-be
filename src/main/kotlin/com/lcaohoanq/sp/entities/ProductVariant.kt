package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "product_variants")
class ProductVariant(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "product_variants_seq", sequenceName = "product_variants_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_variants_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var productId: Int = 0
    var variantName: String = ""
    var priceAdjustment: String = ""
    var stock: Int = 0

}