package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.categories.Category
import com.lcaohoanq.sp.domains.categories.CategoryPort

fun Category.toCategoryResponse(): CategoryPort.CategoryRes {
    return CategoryPort.CategoryRes(
        id = this.id!!,
        name = this.name,
        description = this.description,
        parentId = this.parentId,
        createdAt = this.createdAt,
        createdBy = this.createdBy,
        lastModifiedBy = this.lastModifiedBy,
        lastModifiedOn = this.lastModifiedOn
    )
}