package com.lcaohoanq.sp.domains.cart

import jakarta.validation.constraints.*
import java.time.LocalDateTime

object CartPort {

    data class AddToCartRequest(
        @field:NotNull(message = "Product ID is required")
        @field:Positive(message = "Product ID must be positive")
        val productId: Long,

        @field:NotNull(message = "Quantity is required")
        @field:Min(value = 1, message = "Quantity must be at least 1")
        @field:Max(value = 999, message = "Quantity cannot exceed 999")
        val quantity: Int
    )

    data class UpdateCartItemRequest(
        @field:NotNull(message = "Quantity is required")
        @field:Min(value = 1, message = "Quantity must be at least 1")
        @field:Max(value = 999, message = "Quantity cannot exceed 999")
        val quantity: Int
    )

    data class CartItemResponse(
        val id: Long,
        val productId: Long,
        val productName: String,
        val productImage: String?,
        val priceAtTime: Double,
        val quantity: Int,
        val totalPrice: Double,
        val isAvailable: Boolean
    )

    data class CartResponse(
        val id: Long,
        val userId: Long,
        val items: List<CartItemResponse>,
        val totalAmount: Double,
        val totalItems: Int,
        val isEmpty: Boolean,
        val createdAt: LocalDateTime?,
        val updatedAt: LocalDateTime?
    )

    data class CartSummaryResponse(
        val totalAmount: Double,
        val totalItems: Int,
        val totalUniqueProducts: Int,
        val hasUnavailableItems: Boolean,
        val unavailableItems: List<CartItemResponse>
    )

    data class MoveToCartRequest(
        @field:NotEmpty(message = "Product IDs cannot be empty")
        val productIds: List<Long>
    )
}
