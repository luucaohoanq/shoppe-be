package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "product_images")
class ProductImage(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "product_images_seq", sequenceName = "product_images_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_images_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var productId:Int=0
    var imageUrl:String=""
    var isPrimary:Boolean=false

}