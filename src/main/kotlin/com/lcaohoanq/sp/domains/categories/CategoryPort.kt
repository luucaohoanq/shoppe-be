package com.lcaohoanq.sp.domains.categories

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

interface CategoryPort {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class CategoryRes(
        val id: Long,
        val name: String,
        val description: String? = null,
        val parentId: Long? = null,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        val createdAt: LocalDateTime? = null,
        val createdBy: String? = null,
        val lastModifiedBy: String? = null,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        var lastModifiedOn: LocalDateTime? = null,
    )


    data class CategoryTreeResponse(
        val id: Long,
        val name: String,
        val description: String? = null,
        val img: String? = null,
        val subcategories: List<SubCategoryResponse>
    )

    data class SubCategoryResponse(
        val id: Long,
        val name: String
    )

}