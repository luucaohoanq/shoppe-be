package com.lcaohoanq.sp.metadata

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.SortableField
import org.springframework.data.domain.Pageable


data class QueryCriteria<T : SortableField>(
    val search: String = "",
    val sortBy: T,
    val sortOrder: SortOrder = SortOrder.ASC,
    val filters: Map<String, Any?> = emptyMap()
)