package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.order.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface OrderRepository : JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    
    fun findByUserId(userId: Long): List<Order>
    fun findByUserId(userId: Long, pageable: Pageable): Page<Order>
    
    fun findByUserIdAndStatus(userId: Long, status: Order.OrderStatus): List<Order>
    fun findByUserIdAndStatus(userId: Long, status: Order.OrderStatus, pageable: Pageable): Page<Order>
    
    fun findByUserIdAndStatusOrderByCreatedAtDesc(userId: Long, status: Order.OrderStatus, pageable: Pageable): Page<Order>
    
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Order>
    
    fun findByStatusOrderByCreatedAtDesc(status: Order.OrderStatus, pageable: Pageable): Page<Order>
    
    fun countByUserIdAndStatus(userId: Long, status: Order.OrderStatus): Long
    
    fun findByOrderNumber(orderNumber: String): Order?
    
    fun findByStatus(status: Order.OrderStatus): List<Order>
    fun findByStatus(status: Order.OrderStatus, pageable: Pageable): Page<Order>
    
    fun findByPaymentStatus(paymentStatus: Order.PaymentStatus): List<Order>
    fun findByShippingStatus(shippingStatus: Order.ShippingStatus): List<Order>
    
    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    fun findByUserIdOrderByCreatedAtDesc(@Param("userId") userId: Long, pageable: Pageable): Page<Order>
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    fun findByCreatedAtBetween(
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Order>
    
    @Query("SELECT o FROM Order o WHERE o.totalAmount BETWEEN :minAmount AND :maxAmount")
    fun findByTotalAmountBetween(
        @Param("minAmount") minAmount: Double,
        @Param("maxAmount") maxAmount: Double
    ): List<Order>
    
    // Statistics queries
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    fun countByStatus(@Param("status") status: Order.OrderStatus): Long
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.paymentStatus = :paymentStatus")
    fun countByPaymentStatus(@Param("paymentStatus") paymentStatus: Order.PaymentStatus): Long
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED'")
    fun getTotalRevenue(): Double?
    
    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.status != 'CANCELLED'")
    fun getAverageOrderValue(): Double?
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.userId = :userId")
    fun countByUserId(@Param("userId") userId: Long): Long
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.userId = :userId AND o.status = 'DELIVERED'")
    fun getTotalSpentByUser(@Param("userId") userId: Long): Double?
    
    // Recent orders
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :since ORDER BY o.createdAt DESC")
    fun findRecentOrders(@Param("since") since: LocalDateTime, pageable: Pageable): Page<Order>
    
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    fun findRecentOrders(pageable: Pageable): Page<Order>
    
    // Orders requiring attention
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt < :olderThan")
    fun findOverdueOrders(@Param("status") status: Order.OrderStatus, @Param("olderThan") olderThan: LocalDateTime): List<Order>
    
    // Orders requiring attention
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.createdAt < :olderThan")
    fun findPendingOrdersOlderThan(@Param("olderThan") olderThan: LocalDateTime): List<Order>
    
    @Query("SELECT o FROM Order o WHERE o.estimatedDelivery < :date AND o.shippingStatus != 'DELIVERED'")
    fun findOverdueDeliveries(@Param("date") date: LocalDateTime): List<Order>
    
    // Advanced search
    @Query("""
        SELECT o FROM Order o 
        WHERE (:orderNumber IS NULL OR o.orderNumber LIKE %:orderNumber%)
        AND (:userId IS NULL OR o.userId = :userId)
        AND (:status IS NULL OR o.status = :status)
        AND (:startDate IS NULL OR o.createdAt >= :startDate)
        AND (:endDate IS NULL OR o.createdAt <= :endDate)
        AND (:minAmount IS NULL OR o.totalAmount >= :minAmount)
        AND (:maxAmount IS NULL OR o.totalAmount <= :maxAmount)
        ORDER BY o.createdAt DESC
    """)
    fun findOrdersWithCriteria(
        @Param("orderNumber") orderNumber: String?,
        @Param("userId") userId: Long?,
        @Param("status") status: Order.OrderStatus?,
        @Param("startDate") startDate: LocalDateTime?,
        @Param("endDate") endDate: LocalDateTime?,
        @Param("minAmount") minAmount: Double?,
        @Param("maxAmount") maxAmount: Double?,
        pageable: Pageable
    ): Page<Order>
    
    @Query("""
        SELECT o FROM Order o 
        WHERE (:userId IS NULL OR o.userId = :userId)
        AND (:status IS NULL OR o.status = :status)
        AND (:paymentStatus IS NULL OR o.paymentStatus = :paymentStatus)
        AND (:shippingStatus IS NULL OR o.shippingStatus = :shippingStatus)
        AND (:fromDate IS NULL OR o.createdAt >= :fromDate)
        AND (:toDate IS NULL OR o.createdAt <= :toDate)
        AND (:minAmount IS NULL OR o.totalAmount >= :minAmount)
        AND (:maxAmount IS NULL OR o.totalAmount <= :maxAmount)
        ORDER BY o.createdAt DESC
    """)
    fun findOrdersWithFilters(
        @Param("userId") userId: Long?,
        @Param("status") status: Order.OrderStatus?,
        @Param("paymentStatus") paymentStatus: Order.PaymentStatus?,
        @Param("shippingStatus") shippingStatus: Order.ShippingStatus?,
        @Param("fromDate") fromDate: LocalDateTime?,
        @Param("toDate") toDate: LocalDateTime?,
        @Param("minAmount") minAmount: Double?,
        @Param("maxAmount") maxAmount: Double?,
        pageable: Pageable
    ): Page<Order>
}
