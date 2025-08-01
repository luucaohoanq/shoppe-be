package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.payment.Payment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PaymentRepository : JpaRepository<Payment, Long> {
    
    fun findByOrderId(orderId: Long): Payment?
    
    fun findByTransactionId(transactionId: String): Payment?
    
//    fun findByStatus(status: Payment.PaymentStatus): List<Payment>
//    fun findByStatus(status: Payment.PaymentStatus, pageable: Pageable): Page<Payment>
//
//    fun findByPaymentMethod(paymentMethod: Payment.PaymentMethod): List<Payment>
//    fun findByPaymentMethod(paymentMethod: Payment.PaymentMethod, pageable: Pageable): Page<Payment>
    
    fun findByStatusOrderByCreatedAtDesc(status: Payment.PaymentStatus, pageable: Pageable): Page<Payment>
    
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Payment>
    
    @Query("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    fun findByCreatedAtBetween(
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Payment>
    
    @Query("SELECT p FROM Payment p WHERE p.amount BETWEEN :minAmount AND :maxAmount")
    fun findByAmountBetween(
        @Param("minAmount") minAmount: Double,
        @Param("maxAmount") maxAmount: Double
    ): List<Payment>
    
    // Statistics queries
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    fun countByStatus(@Param("status") status: Payment.PaymentStatus): Long
    
    @Query("SELECT SUM(p.amount) FROM Payment p")
    fun getTotalAmount(): Double?
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    fun getTotalAmountByStatus(@Param("status") status: Payment.PaymentStatus): Double?
    
//    @Query("SELECT AVG(p.amount) FROM Payment p WHERE p.status = 'COMPLETED'")
//    fun getAveragePaymentAmount(): Double?
    
    // Advanced search
    @Query("""
        SELECT p FROM Payment p 
        WHERE (:orderId IS NULL OR p.orderId = :orderId)
        AND (:status IS NULL OR p.status = :status)
        AND (:paymentMethod IS NULL OR p.paymentMethod = :paymentMethod)
        AND (:startDate IS NULL OR p.createdAt >= :startDate)
        AND (:endDate IS NULL OR p.createdAt <= :endDate)
        AND (:minAmount IS NULL OR p.amount >= :minAmount)
        AND (:maxAmount IS NULL OR p.amount <= :maxAmount)
        ORDER BY p.createdAt DESC
    """)
    fun findPaymentsWithCriteria(
        @Param("orderId") orderId: Long?,
        @Param("status") status: Payment.PaymentStatus?,
        @Param("paymentMethod") paymentMethod: Payment.PaymentMethod?,
        @Param("startDate") startDate: LocalDateTime?,
        @Param("endDate") endDate: LocalDateTime?,
        @Param("minAmount") minAmount: Double?,
        @Param("maxAmount") maxAmount: Double?,
        pageable: Pageable
    ): Page<Payment>
    
    // Recent payments
//    @Query("SELECT p FROM Payment p WHERE p.createdAt >= :since ORDER BY p.createdAt DESC")
//    fun findRecentPayments(@Param("since") since: LocalDateTime, pageable: Pageable): Page<Payment>
//
//    // Failed payments within a time period
//    @Query("SELECT p FROM Payment p WHERE p.status = 'FAILED' AND p.createdAt >= :since")
//    fun findRecentFailedPayments(@Param("since") since: LocalDateTime): List<Payment>
//
//    // Pending payments older than specified time
//    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' AND p.createdAt < :olderThan")
//    fun findPendingPaymentsOlderThan(@Param("olderThan") olderThan: LocalDateTime): List<Payment>
}
