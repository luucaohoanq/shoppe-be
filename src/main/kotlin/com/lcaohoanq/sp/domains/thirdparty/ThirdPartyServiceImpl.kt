package com.lcaohoanq.sp.domains.thirdparty

import com.lcaohoanq.sp.domains.currency.ExchangeRateResponse
import com.lcaohoanq.sp.enums.Currency
import com.lcaohoanq.sp.exceptions.base.DataNotFoundException
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
@Slf4j
class ThirdPartyServiceImpl(
    private val webClient: WebClient
): ThirdPartyService {
    override fun getBaseCurrencyRate(base: Currency): ExchangeRateResponse {
        val rates =
            webClient.get().uri("https://api.exchangerate-api.com/v4/latest/$base").retrieve()
                .bodyToMono(ExchangeRateResponse::class.java)
                .block()

        if (rates == null || rates.rates.isEmpty()) {
            throw DataNotFoundException("Currency rates not found")
        }

        return rates
    }
}