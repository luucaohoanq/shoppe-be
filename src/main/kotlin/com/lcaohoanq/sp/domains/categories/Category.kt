package com.lcaohoanq.sp.domains.categories

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    val name: String = "New Category",
    val description: String? = null,
    val parentId: Long? = null, // null if it's a top-level category

): BaseEntity() {
}