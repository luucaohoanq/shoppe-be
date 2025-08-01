package com.lcaohoanq.sp.domains.currency

interface CurrencyRateService {

    fun getAlls(): List<CurrencyRate>

    fun getById(id: Long): CurrencyRate?

    fun create(currencyRate: CurrencyRate): CurrencyRate

    fun update(id: Long, currencyRate: CurrencyRate): CurrencyRate?

    fun delete(id: Long): Boolean

}