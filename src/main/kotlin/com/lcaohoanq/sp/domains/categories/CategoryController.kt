package com.lcaohoanq.sp.domains.categories

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.Sortable
import com.lcaohoanq.sp.utils.createPageRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.prefix}/categories")
@Tag(name = "categories", description = "Category API")
class CategoryController(
    private val categoryService: CategoryService
) : BaseController() {

    @GetMapping("/all")
    @PreAuthorize("permitAll()")
    @Operation(
        summary = "Get all categories for fast access ease for frontend",
        description = "Retrieve a list of all categories and their subcategories in a tree structure."
    )
    fun getAllCategories(): ResponseEntity<MyApiResponse<List<CategoryPort.CategoryTreeResponse>>> =
        ok(data = categoryService.getAll())

    @Operation(
        summary = "Get paginated categories",
        description = "Retrieve a paginated list of categories with optional search and sorting.",
        security = [SecurityRequirement(name = "keycloak")]
    )
    @GetMapping("/query")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER', 'ROLE_STAFF', 'ROLE_CUSTOMER')")
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

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER', 'ROLE_STAFF')")
    @Operation(
        summary = "Get paged categories",
        description = "Retrieve paginated categories with sorting",
        security = [SecurityRequirement(name = "keycloak")]
    )
    fun getPageable(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "id,asc") sort: String
    ): ResponseEntity<MyApiResponse<Page<Category>>> {
        return ok(data = categoryService.getAll(createPageRequest(page, size, sort)))
    }

    @GetMapping("/parents")
    @PreAuthorize("permitAll()")
    @Operation(
        summary = "Get parent categories",
        description = "Retrieve a list of all parent categories in the system."
    )
    fun getParentCategories(): ResponseEntity<MyApiResponse<List<Category>>> =
        ok(data = categoryService.getParentCategories())

    @GetMapping("/{id}/children")
    @PreAuthorize("permitAll()")
    @Operation(
        summary = "Get child categories of a parent category",
        description = "Retrieve a list of child categories for a given parent category ID."
    )
    fun getChildrenOfParentCategory(
        @PathVariable id: Long
    ): ResponseEntity<MyApiResponse<List<Category>>> {
        return ok(data = categoryService.getChildCategories(id))
    }

}