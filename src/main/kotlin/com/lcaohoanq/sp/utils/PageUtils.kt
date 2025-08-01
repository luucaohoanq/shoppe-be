package com.lcaohoanq.sp.utils

import org.springframework.data.domain.Sort

fun createPageRequest (
    page: Int,
    size: Int,
    sort: String
): org.springframework.data.domain.PageRequest {
    val sortParts = sort.split(",")
    val sortField = if (sortParts.isNotEmpty()) sortParts[0] else "id"
    val sortOrder = if (sortParts.size > 1 && sortParts[1].equals("desc", ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC

    return org.springframework.data.domain.PageRequest.of(page, size, Sort.by(sortOrder, sortField))
}