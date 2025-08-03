package com.lcaohoanq.sp.domains.product

import com.lcaohoanq.sp.domains.discount.Voucher
import com.lcaohoanq.sp.domains.discount.VoucherResponse
import com.lcaohoanq.sp.domains.discount.toResponse
import com.lcaohoanq.sp.enums.DiscountType
import com.lcaohoanq.sp.repositories.VoucherRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.min

@Service
class ProductDiscountService(
    private val voucherRepository: VoucherRepository
) {

    fun calculateProductWithDiscount(product: Product): ProductResponse {
        val availableVouchers = getAvailableVouchersForProduct(product)
        val bestDiscount = findBestDiscount(product, availableVouchers)
        
        val originalPrice = product.price
        val discountedPrice = bestDiscount?.let { 
            applyDiscount(originalPrice, it) 
        } ?: originalPrice
        
        return product.toResponse(
            availableVouchers = availableVouchers.map { it.toResponse() },
            calculatedPrice = discountedPrice,
            originalPrice = originalPrice
        )
    }

    fun calculateProductsWithDiscounts(products: List<Product>): List<ProductResponse> {
        return products.map { calculateProductWithDiscount(it) }
    }

    private fun getAvailableVouchersForProduct(product: Product): List<Voucher> {
        val now = LocalDateTime.now()
        
        // Get vouchers that are:
        // 1. Active and valid (not expired)
        // 2. Not fully used (usedCount < usageLimit)
        // 3. Either general vouchers or specific to this product/category
        return voucherRepository.findAvailableVouchersForProduct(
            productId = product.id,
            categoryId = product.category?.id,
            currentTime = now
        )
    }

    private fun findBestDiscount(product: Product, vouchers: List<Voucher>): Voucher? {
        if (vouchers.isEmpty()) return null

        var bestVoucher: Voucher? = null
        var maxDiscount = 0.0

        for (voucher in vouchers) {
            val discountAmount = calculateDiscountAmount(product.price, voucher)
            if (discountAmount > maxDiscount) {
                maxDiscount = discountAmount
                bestVoucher = voucher
            }
        }

        return bestVoucher
    }

    private fun calculateDiscountAmount(originalPrice: Double, voucher: Voucher): Double {
        return when (voucher.discountType) {
            DiscountType.PERCENTAGE -> {
                val discountAmount = originalPrice * (voucher.discountValue / 100)
                // Apply max discount limit if specified
                voucher.condition?.maxDiscountAmount?.let { maxDiscount ->
                    min(discountAmount, maxDiscount)
                } ?: discountAmount
            }
            DiscountType.AMOUNT -> {
                // Fixed amount discount
                min(voucher.discountValue, originalPrice)
            }
            DiscountType.FREE_SHIPPING -> {
                // For display purposes, we don't change the product price for free shipping
                0.0
            }
            DiscountType.BUY_X_GET_Y -> {
                // This would require cart-level calculation, for single product we can't apply this
                0.0
            }

            DiscountType.FREE_GIFT -> {
                // Free gift doesn't affect the product price directly
                0.0
            }
        }
    }

    private fun applyDiscount(originalPrice: Double, voucher: Voucher): Double {
        val discountAmount = calculateDiscountAmount(originalPrice, voucher)
        return maxOf(0.0, originalPrice - discountAmount)
    }
}
