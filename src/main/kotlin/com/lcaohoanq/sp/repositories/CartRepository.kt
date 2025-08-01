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
