package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "products")
class Product(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "products_seq", sequenceName = "products_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var name: String = ""
    var description: String = ""
    var price: Double = 0.0
    var stock: Int = 0
    var categoryId: Int = 0
    var shopId: Int = 0

}