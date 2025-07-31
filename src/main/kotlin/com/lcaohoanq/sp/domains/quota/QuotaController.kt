package com.lcaohoanq.sp.domains.quota

import com.lcaohoanq.sp.apis.MyApiResponseV2
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.dto.QuotaPort
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.prefix}/quotas")
@Tag(name = "quotas", description = "Quota API, for managing API quotas")
class QuotaController(
    private val quotaService: IApiQuotaService
) : BaseController() {

    @Operation(summary = "Get all quotas", description = "Get all quotas")
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER', 'ROLE_STAFF')")
    fun findAll(): ResponseEntity<MyApiResponseV2<List<QuotaPort.QuotaResponse>>> =
        ok(
            message = "Get all quotas successfully",
            data = quotaService.findAllQuotas()
        )


    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER', 'ROLE_STAFF')")
    fun findByUser(@PathVariable id: Long): ResponseEntity<MyApiResponseV2<List<ApiQuota>>> {
        val quotas = quotaService.findQuotasByUserId(id)

        if (quotas.isNullOrEmpty()) {
            return ResponseEntity.notFound().build()
        }

        return ok(
            message = "Get quotas by user successfully",
            data = quotas
        )
    }


}
