package com.lcaohoanq.sp.domains.categories

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category (

    @Id
    @SequenceGenerator(name = "categories_seq", sequenceName = "categories_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Long? = null,

    val name: String = "New Category",
    val description: String? = null,
    val parentId: Long? = null, // null if it's a top-level category

): BaseEntity() {
}