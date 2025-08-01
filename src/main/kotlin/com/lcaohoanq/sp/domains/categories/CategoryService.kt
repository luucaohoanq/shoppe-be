package com.lcaohoanq.sp.domains.categories

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.utils.Sortable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CategoryService {

    fun getAll(): List<Category>
    fun getAll(pageable: Pageable, queryCriteria: QueryCriteria<Sortable.CategorySortField>):  PageResponse<CategoryPort.CategoryRes>;
    fun getById(id: Long): Category?
    fun getAll(pageable: Pageable): Page<Category>

}