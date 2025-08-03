package com.lcaohoanq.sp.domains.discount

import com.lcaohoanq.sp.enums.DiscountType

import java.time.LocalDateTime

interface VoucherPort {

    data class VoucherRes(
        val id: Long,
        val code: String,
        val description: String?,
        val discountType: DiscountType,
        val discountValue: Double,
        val minPurchaseAmount: Double?,
        val maxDiscountAmount: Double?,
        val validFrom: LocalDateTime,
        val validUntil: LocalDateTime,
        val usageLimit: Int?,
        val usedCount: Int,
        val active: Boolean,
        val createdAt: LocalDateTime,
        val createdBy: String?,
        val lastModifiedBy: String?,
        val lastModifiedOn: LocalDateTime?
    )

}