package com.lcaohoanq.sp.domains.cart

interface ICartService {
    
    // Cart management
    fun getOrCreateCart(userId: Long): CartPort.CartResponse
    fun getCart(userId: Long): CartPort.CartResponse?
    fun clearCart(userId: Long)
    
    // Cart items management
    fun addToCart(userId: Long, request: CartPort.AddToCartRequest): CartPort.CartResponse
    fun updateCartItem(userId: Long, productId: Long, request: CartPort.UpdateCartItemRequest): CartPort.CartResponse
    fun removeFromCart(userId: Long, productId: Long): CartPort.CartResponse
    fun removeMultipleItems(userId: Long, productIds: List<Long>): CartPort.CartResponse
    
    // Cart operations
    fun getCartSummary(userId: Long): CartPort.CartSummaryResponse
    fun validateCartItems(userId: Long): CartPort.CartSummaryResponse
    fun syncCartPrices(userId: Long): CartPort.CartResponse
    
    // Utility methods
    fun getCartItemCount(userId: Long): Int
    fun isProductInCart(userId: Long, productId: Long): Boolean
}
