package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.cart.Cart
import com.lcaohoanq.sp.domains.cart.CartItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

interface CartRepository : JpaRepository<Cart, Long> {
    
    fun findByUserId(userId: Long): Cart?
    
    fun existsByUserId(userId: Long): Boolean
    
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.userId = :userId")
    fun findByUserIdWithItems(@Param("userId") userId: Long): Cart?
    
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cartId = :cartId")
    fun countItemsByCartId(@Param("cartId") cartId: Long): Long
    
    @Query("SELECT SUM(ci.quantity * ci.priceAtTime) FROM CartItem ci WHERE ci.cartId = :cartId")
    fun getTotalAmountByCartId(@Param("cartId") cartId: Long): Double?
}

@Repository
interface CartItemRepository : JpaRepository<CartItem, Long> {
    
    fun findByCartId(cartId: Long): List<CartItem>
    
    fun findByCartIdAndProductId(cartId: Long, productId: Long): CartItem?
    
    fun deleteByCartIdAndProductId(cartId: Long, productId: Long)
    
    fun deleteByCartId(cartId: Long)
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.cartId = :cartId AND ci.productId IN :productIds")
    fun findByCartIdAndProductIdIn(@Param("cartId") cartId: Long, @Param("productIds") productIds: List<Long>): List<CartItem>
    
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cartId = :cartId")
    fun countByCartId(@Param("cartId") cartId: Long): Long
    
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.cartId = :cartId")
    fun getTotalQuantityByCartId(@Param("cartId") cartId: Long): Int?
}
