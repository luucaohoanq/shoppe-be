package com.lcaohoanq.sp.domains.categories

import com.lcaohoanq.sp.bases.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    val name: String = "New Category",
    val description: String? = null,
    val parentId: Long? = null,
    val parentSlug: String? = null, // null if it's a top-level category
    val slug: String, // null if it's a top-level category
    val imageUrl: String? = null,
    val active: Boolean = true,

): BaseEntity() {
}