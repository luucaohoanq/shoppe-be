package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.order.OrderItem
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface OrderItemRepository : JpaRepository<OrderItem, Long> {

    fun findByOrderId(orderId: Long): List<OrderItem>

    fun findByProductId(productId: Long): List<OrderItem>

    @Query("SELECT oi FROM OrderItem oi WHERE oi.orderId = :orderId AND oi.productId = :productId")
    fun findByOrderIdAndProductId(@Param("orderId") orderId: Long, @Param("productId") productId: Long): OrderItem?

    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.productId = :productId")
    fun getTotalQuantitySoldForProduct(@Param("productId") productId: Long): Int?

    @Query("SELECT SUM(oi.totalPrice) FROM OrderItem oi WHERE oi.orderId = :orderId")
    fun getTotalAmountForOrder(@Param("orderId") orderId: Long): Double?

    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.orderId = :orderId")
    fun getItemCountForOrder(@Param("orderId") orderId: Long): Long

    // Top selling products
    @Query("""
        SELECT oi.productId, oi.productName, SUM(oi.quantity) as totalSold
        FROM OrderItem oi 
        JOIN Order o ON oi.orderId = o.id 
        WHERE o.status = 'DELIVERED' 
        GROUP BY oi.productId, oi.productName 
        ORDER BY totalSold DESC
    """)
    fun getTopSellingProducts(pageable: Pageable): List<Array<Any>>
}