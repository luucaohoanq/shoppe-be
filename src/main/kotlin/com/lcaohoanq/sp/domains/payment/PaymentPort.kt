package com.lcaohoanq.sp.domains.payment


import java.time.LocalDateTime

object PaymentPort {
    
    data class CreatePaymentRequest(
        val orderId: Long,
        val amount: Double,
        val paymentMethod: Payment.PaymentMethod,
        val description: String? = null
    )
    
    data class PaymentResponse(
        val id: Long,
        val orderId: Long,
        val amount: Double,
        val paymentMethod: Payment.PaymentMethod,
        val status: Payment.PaymentStatus,
        val transactionId: String?,
        val paymentUrl: String?,
        val description: String?,
        val createdAt: LocalDateTime?,
        val updatedAt: LocalDateTime?
    )
    
    data class PaymentResult(
        val paymentUrl: String?,
        val instructions: String?
    )
    
    data class UpdatePaymentStatusRequest(
        val status: Payment.PaymentStatus,
        val transactionId: String? = null,
        val notes: String? = null
    )
    
    data class PaymentSearchRequest(
        val orderId: Long? = null,
        val status: Payment.PaymentStatus? = null,
        val paymentMethod: Payment.PaymentMethod? = null,
        val startDate: LocalDateTime? = null,
        val endDate: LocalDateTime? = null,
        val minAmount: Double? = null,
        val maxAmount: Double? = null
    )
    
    data class PaymentStatsResponse(
        val totalPayments: Long,
        val successfulPayments: Long,
        val failedPayments: Long,
        val pendingPayments: Long,
        val totalAmount: Double,
        val successfulAmount: Double
    )
}
