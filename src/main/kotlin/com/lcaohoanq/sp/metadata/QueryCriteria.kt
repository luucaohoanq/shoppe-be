package com.lcaohoanq.sp.metadata

import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.SortableField


data class QueryCriteria<T : SortableField>(
    val search: String = "",
    val sortBy: T,
    val sortOrder: SortOrder = SortOrder.ASC,
    val filters: Map<String, Any?> = emptyMap()
)