package com.lcaohoanq.sp.domains.currency

import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.enums.Currency
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "currency_rates")
data class CurrencyRate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    val id: Long = 0,

    @Column(nullable = false)
    val fromCurrency: Currency, // e.g., USD

    @Column(nullable = false)
    val toCurrency: Currency, // e.g., VND

    @Column(nullable = false)
    val rate: BigDecimal,
): BaseEntity(){

}
