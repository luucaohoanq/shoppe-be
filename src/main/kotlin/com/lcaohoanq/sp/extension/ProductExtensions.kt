package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.product.Product
import com.lcaohoanq.sp.domains.product.ProductPort
import java.sql.Timestamp
import java.time.LocalDateTime

fun Product.toProductResponse(): ProductPort.ProductResponse {
    return ProductPort.ProductResponse(
        id = this.id ?: 0L,
        name = this.name,
        description = this.description,
        price = this.price,
        stock = this.stock,
        categoryId = this.categoryId,
        shopId = this.shopId,
        imageUrl = this.imageUrl,
        status = this.status,
        rating = this.rating,
        reviewCount = this.reviewCount,
        soldCount = this.soldCount,
        weight = this.weight,
        dimensions = this.dimensions,
        category = this.category?.toCategoryResponse(),
        createdAt = this.createdAt?.toLocalDateTime(),
        updatedAt = this.lastModifiedOn?.toLocalDateTime(),
        isAvailable = this.isAvailable()
    )
}

fun ProductPort.ProductRequest.toProduct(): Product {
    return Product(
        name = this.name,
        description = this.description,
        price = this.price,
        stock = this.stock,
        categoryId = this.categoryId,
        shopId = this.shopId,
        imageUrl = this.imageUrl,
        status = this.status,
        weight = this.weight,
        dimensions = this.dimensions
    )
}

fun Product.updateFromRequest(request: ProductPort.ProductUpdateRequest): Product {
    request.name?.let { this.name = it }
    request.description?.let { this.description = it }
    request.price?.let { this.price = it }
    request.stock?.let { this.stock = it }
    request.categoryId?.let { this.categoryId = it }
    request.imageUrl?.let { this.imageUrl = it }
    request.weight?.let { this.weight = it }
    request.dimensions?.let { this.dimensions = it }
    request.status?.let { this.status = it }
    return this
}

private fun Timestamp.toLocalDateTime(): LocalDateTime {
    return this.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
}
