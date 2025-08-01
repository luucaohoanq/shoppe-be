package com.lcaohoanq.sp.domains.thirdparty

import com.lcaohoanq.sp.domains.currency.ExchangeRateResponse
import com.lcaohoanq.sp.enums.Currency

interface ThirdPartyService {

    //https://api.exchangerate-api.com/v4/latest/vnd
    fun getBaseCurrencyRate(base: Currency): ExchangeRateResponse
}