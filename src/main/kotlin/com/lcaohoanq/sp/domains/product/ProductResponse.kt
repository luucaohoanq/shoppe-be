package com.lcaohoanq.sp.domains.product

import com.lcaohoanq.sp.domains.categories.Category
import com.lcaohoanq.sp.domains.categories.CategoryPort
import com.lcaohoanq.sp.domains.discount.VoucherResponse
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val price_before_discount: Double,
    val stock: Int,
    val shopId: Long,
    val imageUrl: String?,
    val images: List<String>?,
    val status: String,
    val rating: Double,
    val reviewCount: Int,
    val soldCount: Int,
    val sold: Int, // Alias for soldCount to match frontend expectation
    val weight: Double?,
    val dimensions: String?,
    val sku: String?,
    val featured: Boolean?,
    val category: Category?,
    val isAvailable: Boolean,
    val vouchers: List<VoucherResponse>?,
    val hasDiscount: Boolean,
    val discountPercentage: Double?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

fun Product.toResponse(
    availableVouchers: List<VoucherResponse> = emptyList(),
    calculatedPrice: Double? = null,
    originalPrice: Double? = null
): ProductResponse {
    val priceBeforeDiscount = originalPrice ?: this.price
    val currentPrice = calculatedPrice ?: this.price
    val hasDiscount = priceBeforeDiscount > currentPrice
    val discountPercentage = if (hasDiscount) {
        ((priceBeforeDiscount - currentPrice) / priceBeforeDiscount) * 100
    } else null

    return ProductResponse(
        id = this.id ?: 0L,
        name = this.name,
        description = this.description,
        price = currentPrice,
        price_before_discount = priceBeforeDiscount,
        stock = this.stock,
        shopId = this.shopId,
        imageUrl = this.imageUrl,
        images = this.images,
        status = this.status.name,
        rating = this.rating,
        reviewCount = this.reviewCount,
        soldCount = this.soldCount,
        sold = this.soldCount, // Alias for frontend compatibility
        weight = this.weight,
        dimensions = this.dimensions,
        sku = this.sku,
        featured = this.featured,
        category = this.category,
        isAvailable = this.isAvailable(),
        vouchers = availableVouchers,
        hasDiscount = hasDiscount,
        discountPercentage = discountPercentage,
        createdAt = this.createdAt,
        updatedAt = this.lastModifiedOn
    )
}

fun ProductResponse.toPortResponse(): ProductPort.ProductResponse {
    return ProductPort.ProductResponse(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        price_before_discount = this.price_before_discount,
        stock = this.stock,
        shopId = this.shopId,
        imageUrl = this.imageUrl,
        images = this.images,
        status = Product.ProductStatus.valueOf(this.status),
        rating = this.rating,
        reviewCount = this.reviewCount,
        soldCount = this.soldCount,
        sold = this.sold,
        weight = this.weight,
        dimensions = this.dimensions,
        sku = this.sku,
        featured = this.featured,
        category = this.category?.let { 
            CategoryPort.CategoryRes(
                id = it.id ?: 0L,
                name = it.name,
                description = it.description,
                imageUrl = "", // You might want to add this field to Category entity
                active = true, // You might want to add this field to Category entity
                parentId = it.parentId,
                createdAt = it.createdAt,
                lastModifiedOn = it.lastModifiedOn
            )
        },
        isAvailable = this.isAvailable,
        vouchers = emptyList(), // Convert VoucherResponse back to Voucher if needed
        hasDiscount = this.hasDiscount,
        discountPercentage = this.discountPercentage,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
