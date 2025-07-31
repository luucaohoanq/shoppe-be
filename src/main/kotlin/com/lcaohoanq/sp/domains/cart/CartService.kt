package com.lcaohoanq.sp.domains.cart

import com.lcaohoanq.sp.exceptions.BusinessException
import com.lcaohoanq.sp.exceptions.base.DataNotFoundException
import com.lcaohoanq.sp.extension.toCartItemResponse
import com.lcaohoanq.sp.extension.toCartResponse
import com.lcaohoanq.sp.repositories.CartItemRepository
import com.lcaohoanq.sp.repositories.CartRepository
import com.lcaohoanq.sp.repositories.ProductRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository
) : ICartService {

    private val log = KotlinLogging.logger {}

    override fun getOrCreateCart(userId: Long): CartPort.CartResponse {
        log.info { "Getting or creating cart for user: $userId" }
        
        val cart = cartRepository.findByUserIdWithItems(userId) 
            ?: createNewCart(userId)
        
        return cart.toCartResponse()
    }

    @Transactional(readOnly = true)
    override fun getCart(userId: Long): CartPort.CartResponse? {
        log.info { "Getting cart for user: $userId" }
        
        val cart = cartRepository.findByUserIdWithItems(userId)
        return cart?.toCartResponse()
    }

    override fun clearCart(userId: Long) {
        log.info { "Clearing cart for user: $userId" }
        
        val cart = cartRepository.findByUserId(userId)
            ?: throw DataNotFoundException("Cart not found for user: $userId")
        
        cartItemRepository.deleteByCartId(cart.id!!)
        log.info { "Cart cleared successfully for user: $userId" }
    }

    override fun addToCart(userId: Long, request: CartPort.AddToCartRequest): CartPort.CartResponse {
        log.info { "Adding product ${request.productId} to cart for user: $userId" }
        
        // Validate product exists and is available
        val product = productRepository.findById(request.productId)
            .orElseThrow { DataNotFoundException("Product with ID ${request.productId} not found") }
        
        if (!productRepository.isProductAvailable(request.productId)) {
            throw BusinessException("Product is not available for purchase")
        }
        
        if (product.stock < request.quantity) {
            throw BusinessException("Insufficient stock. Available: ${product.stock}, Requested: ${request.quantity}")
        }
        
        // Get or create cart
        val cart = cartRepository.findByUserId(userId) 
            ?: createNewCart(userId)
        
        // Check if item already exists in cart
        val existingItem = cartItemRepository.findByCartIdAndProductId(cart.id!!, request.productId)
        
        if (existingItem != null) {
            // Update existing item
            val newQuantity = existingItem.quantity + request.quantity
            if (newQuantity > product.stock) {
                throw BusinessException("Total quantity exceeds available stock. Available: ${product.stock}")
            }
            existingItem.quantity = newQuantity
            cartItemRepository.save(existingItem)
        } else {
            // Create new item
            val cartItem = CartItem(
                cartId = cart.id!!,
                productId = request.productId,
                quantity = request.quantity,
                priceAtTime = product.price
            )
            cartItemRepository.save(cartItem)
        }
        
        log.info { "Product added to cart successfully" }
        return getOrCreateCart(userId)
    }

    override fun updateCartItem(
        userId: Long, 
        productId: Long, 
        request: CartPort.UpdateCartItemRequest
    ): CartPort.CartResponse {
        log.info { "Updating cart item for user: $userId, product: $productId" }
        
        val cart = cartRepository.findByUserId(userId)
            ?: throw DataNotFoundException("Cart not found for user: $userId")
        
        val cartItem = cartItemRepository.findByCartIdAndProductId(cart.id!!, productId)
            ?: throw DataNotFoundException("Item not found in cart")
        
        // Validate stock
        val product = productRepository.findById(productId)
            .orElseThrow { DataNotFoundException("Product not found") }
        
        if (request.quantity > product.stock) {
            throw BusinessException("Insufficient stock. Available: ${product.stock}")
        }
        
        cartItem.quantity = request.quantity
        cartItemRepository.save(cartItem)
        
        log.info { "Cart item updated successfully" }
        return getOrCreateCart(userId)
    }

    override fun removeFromCart(userId: Long, productId: Long): CartPort.CartResponse {
        log.info { "Removing product $productId from cart for user: $userId" }
        
        val cart = cartRepository.findByUserId(userId)
            ?: throw DataNotFoundException("Cart not found for user: $userId")
        
        cartItemRepository.deleteByCartIdAndProductId(cart.id!!, productId)
        
        log.info { "Product removed from cart successfully" }
        return getOrCreateCart(userId)
    }

    override fun removeMultipleItems(userId: Long, productIds: List<Long>): CartPort.CartResponse {
        log.info { "Removing multiple items from cart for user: $userId" }
        
        val cart = cartRepository.findByUserId(userId)
            ?: throw DataNotFoundException("Cart not found for user: $userId")
        
        productIds.forEach { productId ->
            cartItemRepository.deleteByCartIdAndProductId(cart.id!!, productId)
        }
        
        log.info { "Multiple items removed from cart successfully" }
        return getOrCreateCart(userId)
    }

    @Transactional(readOnly = true)
    override fun getCartSummary(userId: Long): CartPort.CartSummaryResponse {
        log.info { "Getting cart summary for user: $userId" }
        
        val cart = cartRepository.findByUserIdWithItems(userId)
            ?: return CartPort.CartSummaryResponse(
                totalAmount = 0.0,
                totalItems = 0,
                totalUniqueProducts = 0,
                hasUnavailableItems = false,
                unavailableItems = emptyList()
            )
        
        val cartItems = cart.items.map { it.toCartItemResponse() }
        val unavailableItems = cartItems.filter { !it.isAvailable }
        
        return CartPort.CartSummaryResponse(
            totalAmount = cart.getTotalAmount(),
            totalItems = cart.getTotalItems(),
            totalUniqueProducts = cart.items.size,
            hasUnavailableItems = unavailableItems.isNotEmpty(),
            unavailableItems = unavailableItems
        )
    }

    override fun validateCartItems(userId: Long): CartPort.CartSummaryResponse {
        log.info { "Validating cart items for user: $userId" }
        
        val summary = getCartSummary(userId)
        
        if (summary.hasUnavailableItems) {
            log.warn { "Cart has ${summary.unavailableItems.size} unavailable items for user: $userId" }
        }
        
        return summary
    }

    override fun syncCartPrices(userId: Long): CartPort.CartResponse {
        log.info { "Syncing cart prices for user: $userId" }
        
        val cart = cartRepository.findByUserIdWithItems(userId)
            ?: throw DataNotFoundException("Cart not found for user: $userId")
        
        cart.items.forEach { cartItem ->
            val product = productRepository.findById(cartItem.productId)
                .orElse(null)
            
            if (product != null && cartItem.priceAtTime != product.price) {
                cartItem.priceAtTime = product.price
                cartItemRepository.save(cartItem)
                log.info { "Updated price for product ${cartItem.productId} in cart" }
            }
        }
        
        return cart.toCartResponse()
    }

    @Transactional(readOnly = true)
    override fun getCartItemCount(userId: Long): Int {
        val cart = cartRepository.findByUserId(userId) ?: return 0
        return cartItemRepository.getTotalQuantityByCartId(cart.id!!) ?: 0
    }

    @Transactional(readOnly = true)
    override fun isProductInCart(userId: Long, productId: Long): Boolean {
        val cart = cartRepository.findByUserId(userId) ?: return false
        return cartItemRepository.findByCartIdAndProductId(cart.id!!, productId) != null
    }

    private fun createNewCart(userId: Long): Cart {
        log.info { "Creating new cart for user: $userId" }
        
        val cart = Cart(userId = userId)
        return cartRepository.save(cart)
    }
}
