package com.lcaohoanq.sp.domains.payment

import com.lcaohoanq.sp.apis.PageResponse
import org.springframework.data.domain.Pageable

interface IPaymentService {
    
    fun createPayment(orderId: Long, amount: Double, paymentMethod: Payment.PaymentMethod): PaymentPort.PaymentResult
    
    fun createPayment(request: PaymentPort.CreatePaymentRequest): PaymentPort.PaymentResponse
    
    fun processPayment(paymentId: Long): PaymentPort.PaymentResponse
    
    fun getPaymentById(paymentId: Long): PaymentPort.PaymentResponse
    
    fun getPaymentByOrderId(orderId: Long): PaymentPort.PaymentResponse?
    
    fun updatePaymentStatus(paymentId: Long, request: PaymentPort.UpdatePaymentStatusRequest): PaymentPort.PaymentResponse
    
    fun refundPayment(paymentId: Long, reason: String?): PaymentPort.PaymentResponse
    
    fun getAllPayments(pageable: Pageable): PageResponse<PaymentPort.PaymentResponse>
    
    fun searchPayments(request: PaymentPort.PaymentSearchRequest, pageable: Pageable): PageResponse<PaymentPort.PaymentResponse>
    
    fun getPaymentStats(): PaymentPort.PaymentStatsResponse
    
    fun handlePaymentCallback(transactionId: String, status: Payment.PaymentStatus): Boolean
    
    fun cancelPayment(paymentId: Long): PaymentPort.PaymentResponse
    
    fun validatePayment(paymentId: Long): Boolean
    
    fun getFailedPayments(pageable: Pageable): PageResponse<PaymentPort.PaymentResponse>
    
    fun getPendingPayments(pageable: Pageable): PageResponse<PaymentPort.PaymentResponse>
    
    fun generatePaymentUrl(payment: Payment): String?
    
    fun generateTransactionId(): String
}
