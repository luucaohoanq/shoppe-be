package com.lcaohoanq.sp.domains.currency

//https://api.exchangerate-api.com/v4/latest/vnd
data class ExchangeRateResponse(
    val provider: String,
    val base: String,
    val date: String,
    val time_last_updated: Long,
    val rates: Map<String, Double>
)