package com.lcaohoanq.sp.metadata

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lcaohoanq.sp.utils.SortCriterion
import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.Sortable
import com.lcaohoanq.sp.utils.SortableField

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PaginationMeta(
    @JsonProperty("total_pages")
    var totalPages: Int = 0,

    @JsonProperty("total_items")
    var totalItems: Long = 0,

    @JsonProperty("current_page")
    var currentPage: Int = 0,

    @JsonProperty("page_size")
    var pageSize: Int = 0,

    var search: String? = "",

    var sort: SortCriterion<out SortableField>? = SortCriterion(
        Sortable.UserSortField.ID,
        SortOrder.ASC
    )
)
