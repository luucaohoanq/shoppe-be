package com.lcaohoanq.sp.domains.categories

import com.lcaohoanq.sp.apis.MyApiResponseV2
import com.lcaohoanq.sp.repositories.CategoryRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.prefix}/categories")
@Tag(name = "Category", description = "Category API")
class CategoryController(
    private val categoryRepository: CategoryRepository
) {

    @GetMapping("")
    @Operation(
        summary = "Get all categories",
        description = "Retrieve a list of all categories in the system."
    )
    fun getAllCategories(): ResponseEntity<MyApiResponseV2<Any>> {
        val categories = categoryRepository.findAll()
        return MyApiResponseV2.success(data = categories)
    }

}