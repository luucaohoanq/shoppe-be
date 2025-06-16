package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.categories.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, Int> {
}