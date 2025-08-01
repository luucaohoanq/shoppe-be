package com.lcaohoanq.sp.domains.categories

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.repositories.CategoryRepository
import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.Sortable
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.prefix}/categories")
@Tag(name = "categories", description = "Category API")
class CategoryController(
    private val categoryRepository: CategoryRepository,
    private val categoryService: CategoryService
): BaseController() {

    @GetMapping("/all")
    @Operation(
        summary = "Get all categories",
        description = "Retrieve a list of all categories in the system."
    )
    fun getAllCategories(): ResponseEntity<MyApiResponse<Any>> {
        val categories = categoryRepository.findAll()
        return MyApiResponse.success(data = categories)
    }

    @Operation(
        summary = "Get paginated categories",
        description = "Retrieve a paginated list of categories with optional search and sorting."
    )
    @GetMapping("/query")
    fun getCategoriesPaged(
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") limit: Int,
        @RequestParam(required = false, defaultValue = "") search: String,
        @RequestParam(required = false, defaultValue = "ID") sortBy: Sortable.CategorySortField,
        @RequestParam(required = false, defaultValue = "ASC") sortOrder: SortOrder,
    )
            : ResponseEntity<PageResponse<CategoryPort.CategoryRes>> {

        val pageable = PageRequest.of(page, limit)
        val queryCriteria = QueryCriteria(search, sortBy, sortOrder)

        return ResponseEntity.ok(categoryService.getAll(pageable, queryCriteria))
    }


}