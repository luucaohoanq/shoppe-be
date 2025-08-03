package com.lcaohoanq.sp.domains.discount

import com.lcaohoanq.sp.enums.DiscountType
import java.time.LocalDateTime

data class VoucherResponse(
    val id: Long,
    val code: String,
    val description: String,
    val discountType: DiscountType,
    val discountValue: Double,
    val condition: VoucherConditionResponse?,
    val usageLimit: Int,
    val usedCount: Int,
    val active: Boolean,
    val validFrom: LocalDateTime,
    val validUntil: LocalDateTime,
    val isExpired: Boolean,
    val isFullyUsed: Boolean,
    val remainingUses: Int
)

data class VoucherConditionResponse(
    val minPurchaseAmount: Double?,
    val maxDiscountAmount: Double?,
    val requiredCategoryId: Long?,
    val requiredProductId: Long?,
    val giftProductId: Long?
)

fun Voucher.toResponse(): VoucherResponse {
    val now = LocalDateTime.now()
    val isExpired = now.isAfter(this.validUntil)
    val isFullyUsed = this.usedCount >= this.usageLimit
    val remainingUses = maxOf(0, this.usageLimit - this.usedCount)

    return VoucherResponse(
        id = this.id ?: 0L,
        code = this.code,
        description = this.description,
        discountType = this.discountType,
        discountValue = this.discountValue,
        condition = this.condition.let {
            VoucherConditionResponse(
                minPurchaseAmount = it.minPurchaseAmount,
                maxDiscountAmount = it.maxDiscountAmount,
                requiredCategoryId = it.requiredCategoryId,
                requiredProductId = it.requiredProductId,
                giftProductId = it.giftProductId
            )
        },
        usageLimit = this.usageLimit,
        usedCount = this.usedCount,
        active = this.active,
        validFrom = this.validFrom,
        validUntil = this.validUntil,
        isExpired = isExpired,
        isFullyUsed = isFullyUsed,
        remainingUses = remainingUses
    )
}
