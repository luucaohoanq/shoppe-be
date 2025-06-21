package com.lcaohoanq.sp.domains.categories

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.extension.toCategoryResponse
import com.lcaohoanq.sp.metadata.PaginationMeta
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.repositories.CategoryRepository
import com.lcaohoanq.sp.utils.SortCriterion
import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.Sortable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
): CategoryService {
    override fun getAll(): List<Category> {
        return categoryRepository.findAll()
    }

    override fun getAll(
        pageable: Pageable,
        queryCriteria: QueryCriteria<Sortable.CategorySortField>
    ): PageResponse<CategoryPort.CategoryRes> {
        val searchSpecification = CategorySpecification(queryCriteria.search)

        val sortField = queryCriteria.sortBy.field
        val sortOrder =
            if (queryCriteria.sortOrder == SortOrder.ASC) Sort.Direction.ASC else Sort.Direction.DESC

        val sort = Sort.by(sortOrder, sortField)

        val pageResult = categoryRepository.findAll(
            searchSpecification,
            PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
        )

        return PageResponse(
            message = "Get users successfully with query",
            data = pageResult.content.map { it.toCategoryResponse() },
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size,
                search = queryCriteria.search,
                sort = SortCriterion(
                    sortBy = queryCriteria.sortBy,
                    order = queryCriteria.sortOrder
                )
            )
        )
    }

    override fun getById(id: Long): Category? {
        TODO("Not yet implemented")
    }
}