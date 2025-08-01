package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.currency.CurrencyRate
import org.springframework.data.jpa.repository.JpaRepository

interface CurrencyRateRepository : JpaRepository<CurrencyRate, Long> {
}