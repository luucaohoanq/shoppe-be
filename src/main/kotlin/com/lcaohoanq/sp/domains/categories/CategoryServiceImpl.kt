package com.lcaohoanq.sp.domains.categories

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.extension.toCategoryResponse
import com.lcaohoanq.sp.metadata.PaginationMeta
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.repositories.CategoryRepository
import com.lcaohoanq.sp.utils.SortCriterion
import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.Sortable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
): CategoryService {
    override fun getAll(): List<CategoryPort.CategoryTreeResponse> {
        val categories = categoryRepository.findAll()
        return categories.toCategoryTreeResponses()
    }

    fun List<Category>.toCategoryTreeResponses(): List<CategoryPort.CategoryTreeResponse> {
        val categoryBySlug = this.groupBy { it.parentSlug }

        fun buildCategoryTree(category: Category): CategoryPort.CategoryTreeResponse {
            return CategoryPort.CategoryTreeResponse(
                id = category.id!!,
                name = category.name,
                description = category.description,
                img = category.imageUrl,
                subcategories = categoryBySlug[category.slug]
                    ?.sortedBy { it.id } // sort subcategories by id
                    ?.map { CategoryPort.SubCategoryResponse(it.id!!, it.name) }
                    ?: emptyList()
            )
        }

        return categoryBySlug[null]
            ?.sortedBy { it.id } // sort root categories by id
            ?.map { buildCategoryTree(it) }
            ?: emptyList()
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

    override fun getAll(pageable: Pageable): Page<Category> {
        return categoryRepository.findAll(pageable)
    }

    override fun getParentCategories(): List<Category> {
        val children = categoryRepository.findByParentId(-1)
        return children.ifEmpty {
            throw NoSuchElementException("No parent categories found")
        }
    }

    override fun getChildCategories(parentId: Long): List<Category> {
        val children = categoryRepository.findByParentId(parentId)
        return children.ifEmpty {
            throw NoSuchElementException("No child categories found for parent ID: $parentId")
        }
    }
}