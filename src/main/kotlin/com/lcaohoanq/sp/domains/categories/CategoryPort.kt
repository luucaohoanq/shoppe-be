package com.lcaohoanq.sp.domains.categories

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.sql.Timestamp

interface CategoryPort {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class CategoryRes(
        val id: Long,
        val name: String,
        val description: String? = null,
        val parentId: Long? = null,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        val createdAt: Timestamp? = null,
        val createdBy: String? = null,
        val lastModifiedBy: String? = null,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        var lastModifiedOn: Timestamp? = null,
    )

}