package com.lcaohoanq.sp.domains.discount

import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.enums.DiscountType
import jakarta.persistence.*
import net.minidev.json.annotate.JsonIgnore
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "vouchers")
class Voucher(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val code: String,

    val name: String = "New Voucher",

    val description: String,

    @Enumerated(EnumType.STRING)
    val discountType: DiscountType,
    val discountValue: Double,

    @Embedded
    val condition: VoucherCondition = VoucherCondition(),

    val validFrom: LocalDateTime? = LocalDateTime.now(),
    val validUntil: LocalDateTime? = LocalDateTime.now().plusDays(5),

    var expired: Boolean = false,
    var active: Boolean = true,
    var usageLimit: Int? = 1,
    var usedCount: Int = 0,

    @OneToMany(mappedBy = "voucher", cascade = [CascadeType.ALL])
    @JsonIgnore
    var productVouchers: MutableSet<ProductVoucher> = mutableSetOf()

) : BaseEntity() {

    val moreDetail: String
        get() = buildVoucherDetail()

    private fun buildVoucherDetail(): String {
        val details = mutableListOf<String>()

        // Main voucher description
        details.add("Voucher '$name' ($code)")

        // Discount information
        when (discountType) {
            DiscountType.PERCENTAGE -> {
                details.add("Get ${discountValue.toInt()}% discount")
                condition.maxDiscountAmount?.let {
                    details.add("Maximum discount: ${formatCurrency(it)}")
                }
            }
            DiscountType.AMOUNT -> {
                details.add("Get ${formatCurrency(discountValue)} off")
            }
            DiscountType.FREE_SHIPPING -> {
                details.add("Enjoy free shipping")
            }
            DiscountType.BUY_X_GET_Y -> {
                val buyX = condition.buyQuantityX ?: 1
                val getY = condition.getQuantityY ?: 1
                details.add("Buy $buyX get $getY free")
            }
            DiscountType.FREE_GIFT -> {
                details.add("Receive a free gift with your purchase")
                condition.giftProductId?.let {
                    details.add("Gift product ID: $it")
                }
            }
        }

        // Purchase conditions
        condition.minPurchaseAmount?.let { minAmount ->
            details.add("Minimum order value: ${formatCurrency(minAmount)}")
        }

        // Category restrictions
        condition.requiredCategoryId?.let { categoryId ->
            details.add("Valid only for category ID: $categoryId")
        }

        // Product restrictions
        condition.requiredProductId?.let { productId ->
            details.add("Valid only for product ID: $productId")
        }

        // Usage information
        usageLimit?.let { limit ->
            val remaining = limit - usedCount
            when {
                remaining <= 0 -> details.add("⚠️ No uses remaining")
                remaining <= 5 -> details.add("⚠️ Only $remaining uses left")
                else -> details.add("$remaining uses remaining out of $limit")
            }
        }

        // Validity period
        val now = LocalDateTime.now()
        when {
            expired -> details.add("❌ This voucher has expired")
            !active -> details.add("❌ This voucher is currently inactive")
            validFrom?.isAfter(now) == true -> {
                details.add("⏰ Valid from: ${validFrom?.toLocalDate()}")
            }
            validUntil?.isBefore(now) == true -> {
                details.add("❌ Expired on: ${validUntil?.toLocalDate()}")
            }
            else -> {
                validUntil?.let { until ->
                    val daysLeft = java.time.temporal.ChronoUnit.DAYS.between(now.toLocalDate(), until.toLocalDate())
                    when {
                        daysLeft <= 0 -> details.add("⚠️ Expires today")
                        daysLeft <= 3 -> details.add("⚠️ Expires in $daysLeft days (${until.toLocalDate()})")
                        else -> details.add("Valid until: ${until.toLocalDate()}")
                    }
                }
            }
        }

        // Terms and conditions
        details.add("📱 Valid on Shoppe Application only")

        return details.joinToString(" • ")
    }

    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return "${formatter.format(amount)} VND"
    }

    // Additional helper methods
    fun isValid(): Boolean {
        val now = LocalDateTime.now()
        return active &&
                !expired &&
                (validFrom?.isBefore(now) != false) &&
                (validUntil?.isAfter(now) != false) &&
                (usageLimit?.let { it > usedCount } != false)
    }

    fun canBeUsed(): Boolean {
        return isValid() && (usageLimit == null || usedCount < usageLimit!!)
    }

    fun getDiscountDescription(): String {
        return when (discountType) {
            DiscountType.PERCENTAGE -> "${discountValue.toInt()}% OFF"
            DiscountType.AMOUNT -> "${formatCurrency(discountValue)} OFF"
            DiscountType.FREE_SHIPPING -> "FREE SHIPPING"
            DiscountType.BUY_X_GET_Y -> "BUY ${condition.buyQuantityX ?: 1} GET ${condition.getQuantityY ?: 1}"
            DiscountType.FREE_GIFT -> "FREE GIFT"
        }
    }

    fun getShortDescription(): String {
        val discount = getDiscountDescription()
        val minPurchase = condition.minPurchaseAmount?.let { " on orders over ${formatCurrency(it)}" } ?: ""
        return "$discount$minPurchase"
    }
}