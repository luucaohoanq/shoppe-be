package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.product.Product
import com.lcaohoanq.sp.domains.product.ProductPort

fun ProductPort.ProductRequest.toProduct(): Product {
    return Product(
        name = this.name,
        description = this.description,
        price = this.price,
        stock = this.stock,
        shopId = this.shopId,
        imageUrl = this.imageUrl,
        images = this.images,
        weight = this.weight,
        dimensions = this.dimensions
    )
}

fun Product.updateFromRequest(request: ProductPort.ProductUpdateRequest): Product {
    request.name?.let { this.name = it }
    request.description?.let { this.description = it }
    request.price?.let { this.price = it }
    request.stock?.let { this.stock = it }
    request.imageUrl?.let { this.imageUrl = it }
    request.weight?.let { this.weight = it }
    request.dimensions?.let { this.dimensions = it }
    request.status?.let { this.status = it }
    return this
}
