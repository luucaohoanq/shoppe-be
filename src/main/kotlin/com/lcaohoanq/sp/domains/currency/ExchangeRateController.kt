package com.lcaohoanq.sp.domains.currency

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.prefix}/currency-rates")
@Tag(name = "currency-rates", description = "Currency rates management")
class ExchangeRateController(
    private val currencyRateService: CurrencyRateService,
) : BaseController() {

    @GetMapping("")
    fun getAllCurrencyRates(): ResponseEntity<MyApiResponse<Any>> = ok(
        message = "Get all currency rates successfully",
        data = currencyRateService.getAlls()
    )

}