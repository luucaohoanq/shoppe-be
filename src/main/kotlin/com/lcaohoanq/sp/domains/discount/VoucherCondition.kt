package com.lcaohoanq.sp.domains.discount

import com.lcaohoanq.sp.enums.DeviceType
import jakarta.persistence.Embeddable
import kotlin.collections.mutableListOf

@Embeddable
class VoucherCondition(
    val minPurchaseAmount: Double? = null,
    val maxDiscountAmount: Double? = null,
    val buyQuantityX: Int? = null,
    val getQuantityY: Int? = null,
    val giftProductId: Long? = null,
    val requiredProductId: Long? = null,
    val requiredCategoryId: Long? = null,
    val deviceType: List<DeviceType> = mutableListOf( DeviceType.WEB, DeviceType.IOS, DeviceType.ANDROID),
)
