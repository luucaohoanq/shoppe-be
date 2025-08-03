package com.lcaohoanq.sp.domains.headquarters

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.prefix}/headquarters")
@Tag(name = "headquarters", description = "Headquarters API")
class HeadquartersController(
    private val headquartersService: HeadquartersService
) : BaseController() {

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(
        summary = "Get all headquarters",
        description = "Retrieve a list of all Shopee headquarters with their regions and domain URLs."
    )
    fun getAllHeadquarters(): ResponseEntity<MyApiResponse<List<Headquarters>>> {
        val headquarters = headquartersService.getAllHeadquarters()
        return ok(data = headquarters)
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(
        summary = "Get headquarters by ID",
        description = "Retrieve a specific headquarters by its ID."
    )
    fun getHeadquartersById(@PathVariable id: Long): ResponseEntity<MyApiResponse<Headquarters>> {
        val headquarters = headquartersService.getHeadquartersById(id)
        return ok(data = headquarters)
    }

    @GetMapping("/region/{region}")
    @PreAuthorize("permitAll()")
    @Operation(
        summary = "Get headquarters by region",
        description = "Retrieve headquarters information for a specific region code."
    )
    fun getHeadquartersByRegion(@PathVariable region: Int): ResponseEntity<MyApiResponse<Headquarters>> {
        val headquarters = headquartersService.getHeadquartersByRegion(region)
        return ok(data = headquarters)
    }
}
