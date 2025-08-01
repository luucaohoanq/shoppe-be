package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.categories.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface CategoryRepository: JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    fun findByParentId(parentId: Long): List<Category>
}