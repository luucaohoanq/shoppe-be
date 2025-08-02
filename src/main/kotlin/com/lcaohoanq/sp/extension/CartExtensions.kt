package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.cart.Cart
import com.lcaohoanq.sp.domains.cart.CartItem
import com.lcaohoanq.sp.domains.cart.CartPort
import com.lcaohoanq.sp.domains.product.Product
import com.lcaohoanq.sp.repositories.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDateTime

fun Cart.toCartResponse(): CartPort.CartResponse {
    return CartPort.CartResponse(
        id = this.id ?: 0L,
        userId = this.userId,
        items = this.items.map { it.toCartItemResponse() },
        totalAmount = this.getTotalAmount(),
        totalItems = this.getTotalItems(),
        isEmpty = this.isEmpty(),
        createdAt = this.createdAt?.toLocalDateTime(),
        updatedAt = this.lastModifiedOn?.toLocalDateTime()
    )
}

fun CartItem.toCartItemResponse(): CartPort.CartItemResponse {
    return CartPort.CartItemResponse(
        id = this.id ?: 0L,
        productId = this.productId,
        productName = this.product?.name ?: "Unknown Product",
        productImage = this.product?.imageUrl,
        priceAtTime = this.priceAtTime,
        quantity = this.quantity,
        totalPrice = this.getTotalPrice(),
        isAvailable = this.product?.let { 
            it.status == Product.ProductStatus.ACTIVE && it.stock > 0
        } ?: false
    )
}

@Component
class CartItemExtensions @Autowired constructor(
    private val productRepository: ProductRepository
) {
    
    fun CartItem.toCartItemResponseWithProduct(): CartPort.CartItemResponse {
        val product = productRepository.findById(this.productId).orElse(null)
        
        return CartPort.CartItemResponse(
            id = this.id ?: 0L,
            productId = this.productId,
            productName = product?.name ?: "Unknown Product",
            productImage = product?.imageUrl,
            priceAtTime = this.priceAtTime,
            quantity = this.quantity,
            totalPrice = this.getTotalPrice(),
            isAvailable = product?.let { 
                productRepository.isProductAvailable(it.id ?: 0L) 
            } ?: false
        )
    }
}
