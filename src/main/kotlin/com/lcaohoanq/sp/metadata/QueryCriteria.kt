package com.lcaohoanq.sp.metadata

import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.Sortable
import com.lcaohoanq.sp.utils.SortableField


data class QueryCriteria<T : SortableField>(
    val search: String = "",
    val sortBy: T = Sortable.UserSortField.ID as T,
    val sortOrder: SortOrder = SortOrder.ASC
)
