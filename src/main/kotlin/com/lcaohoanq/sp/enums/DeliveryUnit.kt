package com.lcaohoanq.sp.enums

enum class DeliveryUnit(
    val displayName: String,
    val displayNameEn: String? = null,
    val category: DeliveryCategory
) {
    // Express Services
    SUPER_FAST_4H("Siêu Tốc - 4 Giờ", "Super Fast - 4 Hours", DeliveryCategory.EXPRESS),
    EXPRESS("Hỏa tốc", "Express", DeliveryCategory.EXPRESS),

    // Standard Services
    FAST("Nhanh", "Fast", DeliveryCategory.STANDARD),

    // International Services
    STANDARD_INTERNATIONAL("Quốc tế Tiêu chuẩn", "Standard International", DeliveryCategory.INTERNATIONAL),
    EXPRESS_INTERNATIONAL("Quốc tế Nhanh", "Express International", DeliveryCategory.INTERNATIONAL),
    STANDARD_EXPRESS_LWE("Standard Express - LWE", "Standard Express - LWE", DeliveryCategory.INTERNATIONAL),
    STANDARD_EXPRESS("Standard Express", "Standard Express", DeliveryCategory.INTERNATIONAL),
    SELLER_INTERNATIONAL_FLEET("Seller Own Fleet - Vận chuyển quốc tế", "Seller Own Fleet - International Shipping", DeliveryCategory.INTERNATIONAL),

    // Third-party Delivery Services
    AHAMOVE("AhaMove", "AhaMove", DeliveryCategory.THIRD_PARTY),
    FAHASA("Fahasa", "Fahasa", DeliveryCategory.THIRD_PARTY),
    CON_CUNG_EXPRESS("Con Cưng Express", "Con Cung Express", DeliveryCategory.THIRD_PARTY),
    VIETTEL_POST("Viettel Post", "Viettel Post", DeliveryCategory.THIRD_PARTY),
    VNPOST_FAST("VNPost Nhanh", "VNPost Fast", DeliveryCategory.THIRD_PARTY),
    SPX_EXPRESS("SPX Express", "SPX Express", DeliveryCategory.THIRD_PARTY),
    GIAO_HANG_NHANH("Giao Hàng Nhanh", "Fast Delivery", DeliveryCategory.THIRD_PARTY),
    NINJA_VAN("Ninja Van", "Ninja Van", DeliveryCategory.THIRD_PARTY),
    JT_EXPRESS("J&T Express", "J&T Express", DeliveryCategory.THIRD_PARTY),
    CHOICE_VN("Choice VN", "Choice VN", DeliveryCategory.THIRD_PARTY),

    // Special Services
    BULKY_ITEMS("Hàng Cồng Kềnh", "Bulky Items", DeliveryCategory.SPECIAL),
    PICKUP_LOCKER("Tủ Nhận Hàng", "Pickup Locker", DeliveryCategory.SPECIAL),
    COORDINATING_DELIVERY("Đang Điều Phối ĐVVC", "Coordinating Delivery Service", DeliveryCategory.SPECIAL);

    companion object {
        fun fromDisplayName(displayName: String): DeliveryUnit? {
            return values().find { it.displayName == displayName }
        }

        fun getByCategory(category: DeliveryCategory): List<DeliveryUnit> {
            return values().filter { it.category == category }
        }

        fun getExpressServices(): List<DeliveryUnit> {
            return getByCategory(DeliveryCategory.EXPRESS)
        }

        fun getInternationalServices(): List<DeliveryUnit> {
            return getByCategory(DeliveryCategory.INTERNATIONAL)
        }

        fun getThirdPartyServices(): List<DeliveryUnit> {
            return getByCategory(DeliveryCategory.THIRD_PARTY)
        }
    }
}

fun DeliveryUnit.isExpress(): Boolean = this.category == DeliveryCategory.EXPRESS
fun DeliveryUnit.isInternational(): Boolean = this.category == DeliveryCategory.INTERNATIONAL
fun DeliveryUnit.isThirdParty(): Boolean = this.category == DeliveryCategory.THIRD_PARTY
fun DeliveryUnit.isSpecial(): Boolean = this.category == DeliveryCategory.SPECIAL
