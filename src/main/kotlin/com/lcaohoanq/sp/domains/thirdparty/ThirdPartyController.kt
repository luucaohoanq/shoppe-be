package com.lcaohoanq.sp.domains.thirdparty

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.enums.Currency
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.prefix}/3rd-party")
@Tag(name = "3rd-party", description = "Third-party API integration")
class ThirdPartyController(
    private val thirdPartyService: ThirdPartyService
) : BaseController() {


    @Operation(
        summary = "Get base currency rate from api.exchangerate-api.com",
        description = "Get the exchange rates for a specific base currency"
    )
    @GetMapping("/base/{base}")
    fun getBaseCurrencyRate(@PathVariable base: Currency): ResponseEntity<MyApiResponse<Any>> {
        val data = thirdPartyService.getBaseCurrencyRate(base)

        return ok(
            message = "Get base currency rate successfully",
            data = data
        )
    }



}