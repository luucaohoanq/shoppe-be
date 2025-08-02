package com.lcaohoanq.sp.domains.currency

import com.lcaohoanq.sp.repositories.CurrencyRateRepository
import org.springframework.stereotype.Service

@Service
class CurrencyRateServiceImpl(
    private val currencyRateRepository: CurrencyRateRepository
):CurrencyRateService {
    override fun getAlls(): List<CurrencyRate> {
        return currencyRateRepository.findAll()
    }

    override fun getById(id: Long): CurrencyRate? {
        TODO("Not yet implemented")
    }

    override fun create(currencyRate: CurrencyRate): CurrencyRate {
        TODO("Not yet implemented")
    }

    override fun update(
        id: Long,
        currencyRate: CurrencyRate
    ): CurrencyRate? {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Boolean {
        TODO("Not yet implemented")
    }
}