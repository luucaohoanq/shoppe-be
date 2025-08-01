package com.lcaohoanq.sp.domains.payment

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.exceptions.BusinessException
import com.lcaohoanq.sp.exceptions.base.DataNotFoundException
import com.lcaohoanq.sp.extension.toPaymentResponse
import com.lcaohoanq.sp.metadata.PaginationMeta
import com.lcaohoanq.sp.repositories.OrderRepository
import com.lcaohoanq.sp.repositories.PaymentRepository
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository
) : IPaymentService {

    private val log = KotlinLogging.logger {}

    override fun createPayment(orderId: Long, amount: Double, paymentMethod: Payment.PaymentMethod): PaymentPort.PaymentResult {
        log.info { "Creating payment for order: $orderId, amount: $amount, method: $paymentMethod" }
        
        val order = orderRepository.findById(orderId)
            .orElseThrow { DataNotFoundException("Order not found") }
        
        // Check if payment already exists for this order
        val existingPayment = paymentRepository.findByOrderId(orderId)
        if (existingPayment != null) {
            throw BusinessException("Payment already exists for this order")
        }
        
        val payment = Payment(
            orderId = orderId,
            amount = amount,
            paymentMethod = paymentMethod,
            status = Payment.PaymentStatus.PENDING,
            transactionId = generateTransactionId()
        )
        
        val savedPayment = paymentRepository.save(payment)
        
        // Generate payment URL based on payment method
        val paymentUrl = generatePaymentUrl(savedPayment)
        val instructions = generatePaymentInstructions(paymentMethod)
        
        log.info { "Payment created successfully: ${savedPayment.id}" }
        
        return PaymentPort.PaymentResult(
            paymentUrl = paymentUrl,
            instructions = instructions
        )
    }

    override fun createPayment(request: PaymentPort.CreatePaymentRequest): PaymentPort.PaymentResponse {
        log.info { "Creating payment with request: $request" }
        
        val order = orderRepository.findById(request.orderId)
            .orElseThrow { DataNotFoundException("Order not found") }
        
        val payment = Payment(
            orderId = request.orderId,
            amount = request.amount,
            paymentMethod = request.paymentMethod,
            status = Payment.PaymentStatus.PENDING,
            transactionId = generateTransactionId(),
            description = request.description ?: "",
        )
        
        val savedPayment = paymentRepository.save(payment)
        
        log.info { "Payment created successfully: ${savedPayment.id}" }
        return savedPayment.toPaymentResponse()
    }

    override fun processPayment(paymentId: Long): PaymentPort.PaymentResponse {
        log.info { "Processing payment: $paymentId" }
        
        val payment = paymentRepository.findById(paymentId)
            .orElseThrow { DataNotFoundException("Payment not found") }
        
        if (payment.status != Payment.PaymentStatus.PENDING) {
            throw BusinessException("Payment cannot be processed. Current status: ${payment.status}")
        }
        
        // Simulate payment processing based on payment method
        val success = simulatePaymentProcessing(payment.paymentMethod)
        
        payment.status = if (success) Payment.PaymentStatus.COMPLETED else Payment.PaymentStatus.FAILED
        payment.lastModifiedOn = Timestamp.valueOf(LocalDateTime.now())
        
        val processedPayment = paymentRepository.save(payment)
        
        log.info { "Payment processed successfully: ${processedPayment.id}, status: ${processedPayment.status}" }
        return processedPayment.toPaymentResponse()
    }

    @Transactional(readOnly = true)
    override fun getPaymentById(paymentId: Long): PaymentPort.PaymentResponse {
        log.info { "Getting payment: $paymentId" }
        
        val payment = paymentRepository.findById(paymentId)
            .orElseThrow { DataNotFoundException("Payment not found") }
        
        return payment.toPaymentResponse()
    }

    @Transactional(readOnly = true)
    override fun getPaymentByOrderId(orderId: Long): PaymentPort.PaymentResponse? {
        log.info { "Getting payment for order: $orderId" }
        
        val payment = paymentRepository.findByOrderId(orderId)
        return payment?.toPaymentResponse()
    }

    override fun updatePaymentStatus(paymentId: Long, request: PaymentPort.UpdatePaymentStatusRequest): PaymentPort.PaymentResponse {
        log.info { "Updating payment status for payment: $paymentId to ${request.status}" }
        
        val payment = paymentRepository.findById(paymentId)
            .orElseThrow { DataNotFoundException("Payment not found") }
        
        payment.status = request.status
        payment.lastModifiedOn = Timestamp.valueOf(LocalDateTime.now())
        request.transactionId?.let { payment.transactionId = it }
        request.notes?.let { 
            payment.description = "${payment.description ?: ""}\nNotes: $it"
        }
        
        val updatedPayment = paymentRepository.save(payment)
        
        log.info { "Payment status updated successfully" }
        return updatedPayment.toPaymentResponse()
    }

    override fun refundPayment(paymentId: Long, reason: String?): PaymentPort.PaymentResponse {
        log.info { "Refunding payment: $paymentId" }
        
        val payment = paymentRepository.findById(paymentId)
            .orElseThrow { DataNotFoundException("Payment not found") }
        
        if (payment.status != Payment.PaymentStatus.COMPLETED) {
            throw BusinessException("Payment cannot be refunded. Current status: ${payment.status}")
        }
        
        payment.status = Payment.PaymentStatus.REFUNDED
        payment.lastModifiedOn = Timestamp.valueOf(LocalDateTime.now())
        reason?.let {
            payment.description = "${payment.description ?: ""}\nRefund reason: $it"
        }
        
        val refundedPayment = paymentRepository.save(payment)
        
        log.info { "Payment refunded successfully" }
        return refundedPayment.toPaymentResponse()
    }

    @Transactional(readOnly = true)
    override fun getAllPayments(pageable: Pageable): PageResponse<PaymentPort.PaymentResponse> {
        log.info { "Getting all payments" }
        
        val pageResult = paymentRepository.findAllByOrderByCreatedAtDesc(pageable)
        val paymentResponses = pageResult.content.map { it.toPaymentResponse() }
        
        return PageResponse(
            message = "All payments retrieved successfully",
            data = paymentResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun searchPayments(request: PaymentPort.PaymentSearchRequest, pageable: Pageable): PageResponse<PaymentPort.PaymentResponse> {
        log.info { "Searching payments with criteria: $request" }
        
        val pageResult = paymentRepository.findPaymentsWithCriteria(
            orderId = request.orderId,
            status = request.status,
            paymentMethod = request.paymentMethod,
            startDate = request.startDate,
            endDate = request.endDate,
            minAmount = request.minAmount,
            maxAmount = request.maxAmount,
            pageable = pageable
        )
        
        val paymentResponses = pageResult.content.map { it.toPaymentResponse() }
        
        return PageResponse(
            message = "Payments search completed successfully",
            data = paymentResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getPaymentStats(): PaymentPort.PaymentStatsResponse {
        log.info { "Getting payment statistics" }
        
        val totalPayments = paymentRepository.count()
        val successfulPayments = paymentRepository.countByStatus(Payment.PaymentStatus.COMPLETED)
        val failedPayments = paymentRepository.countByStatus(Payment.PaymentStatus.FAILED)
        val pendingPayments = paymentRepository.countByStatus(Payment.PaymentStatus.PENDING)
        val totalAmount = paymentRepository.getTotalAmount() ?: 0.0
        val successfulAmount = paymentRepository.getTotalAmountByStatus(Payment.PaymentStatus.COMPLETED) ?: 0.0
        
        return PaymentPort.PaymentStatsResponse(
            totalPayments = totalPayments,
            successfulPayments = successfulPayments,
            failedPayments = failedPayments,
            pendingPayments = pendingPayments,
            totalAmount = totalAmount,
            successfulAmount = successfulAmount
        )
    }

    override fun handlePaymentCallback(transactionId: String, status: Payment.PaymentStatus): Boolean {
        log.info { "Handling payment callback for transaction: $transactionId, status: $status" }
        
        val payment = paymentRepository.findByTransactionId(transactionId)
            ?: throw DataNotFoundException("Payment not found for transaction: $transactionId")
        
        payment.status = status
        payment.lastModifiedOn = Timestamp.valueOf(LocalDateTime.now())
        
        paymentRepository.save(payment)
        
        log.info { "Payment callback handled successfully" }
        return true
    }

    override fun cancelPayment(paymentId: Long): PaymentPort.PaymentResponse {
        log.info { "Cancelling payment: $paymentId" }
        
        val payment = paymentRepository.findById(paymentId)
            .orElseThrow { DataNotFoundException("Payment not found") }
        
        if (payment.status != Payment.PaymentStatus.PENDING) {
            throw BusinessException("Payment cannot be cancelled. Current status: ${payment.status}")
        }
        
        payment.status = Payment.PaymentStatus.CANCELLED
        payment.lastModifiedOn = Timestamp.valueOf(LocalDateTime.now())
        
        val cancelledPayment = paymentRepository.save(payment)
        
        log.info { "Payment cancelled successfully" }
        return cancelledPayment.toPaymentResponse()
    }

    @Transactional(readOnly = true)
    override fun validatePayment(paymentId: Long): Boolean {
        val payment = paymentRepository.findById(paymentId).orElse(null) ?: return false
        
        // Validate payment is in a valid state
        return payment.status in listOf(Payment.PaymentStatus.PENDING, Payment.PaymentStatus.COMPLETED) &&
                payment.amount > 0
    }

    @Transactional(readOnly = true)
    override fun getFailedPayments(pageable: Pageable): PageResponse<PaymentPort.PaymentResponse> {
        log.info { "Getting failed payments" }
        
        val pageResult = paymentRepository.findByStatusOrderByCreatedAtDesc(Payment.PaymentStatus.FAILED, pageable)
        val paymentResponses = pageResult.content.map { it.toPaymentResponse() }
        
        return PageResponse(
            message = "Failed payments retrieved successfully",
            data = paymentResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getPendingPayments(pageable: Pageable): PageResponse<PaymentPort.PaymentResponse> {
        log.info { "Getting pending payments" }
        
        val pageResult = paymentRepository.findByStatusOrderByCreatedAtDesc(Payment.PaymentStatus.PENDING, pageable)
        val paymentResponses = pageResult.content.map { it.toPaymentResponse() }
        
        return PageResponse(
            message = "Pending payments retrieved successfully",
            data = paymentResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    override fun generatePaymentUrl(payment: Payment): String? {
        return when (payment.paymentMethod) {
            Payment.PaymentMethod.CREDIT_CARD -> "https://payment-gateway.com/cc/${payment.transactionId}"
            Payment.PaymentMethod.PAYPAL -> "https://paypal.com/checkout/${payment.transactionId}"
            Payment.PaymentMethod.BANK_TRANSFER -> null // No URL for bank transfer
            Payment.PaymentMethod.CASH_ON_DELIVERY -> null // No URL for COD
            Payment.PaymentMethod.DIGITAL_WALLET -> "https://wallet.com/pay/${payment.transactionId}"
        }
    }

    override fun generateTransactionId(): String {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val random = (10000..99999).random()
        return "TXN-$timestamp-$random"
    }

    private fun simulatePaymentProcessing(paymentMethod: Payment.PaymentMethod): Boolean {
        // Simulate payment processing with different success rates
        return when (paymentMethod) {
            Payment.PaymentMethod.CREDIT_CARD -> (1..100).random() <= 95 // 95% success rate
            Payment.PaymentMethod.PAYPAL -> (1..100).random() <= 98 // 98% success rate
            Payment.PaymentMethod.BANK_TRANSFER -> (1..100).random() <= 90 // 90% success rate
            Payment.PaymentMethod.CASH_ON_DELIVERY -> true // Always success
            Payment.PaymentMethod.DIGITAL_WALLET -> (1..100).random() <= 97 // 97% success rate
        }
    }

    private fun generatePaymentInstructions(paymentMethod: Payment.PaymentMethod): String {
        return when (paymentMethod) {
            Payment.PaymentMethod.CREDIT_CARD -> "Please complete payment using your credit card details."
            Payment.PaymentMethod.PAYPAL -> "You will be redirected to PayPal to complete your payment."
            Payment.PaymentMethod.BANK_TRANSFER -> "Please transfer the amount to our bank account. Details will be provided separately."
            Payment.PaymentMethod.CASH_ON_DELIVERY -> "Payment will be collected upon delivery."
            Payment.PaymentMethod.DIGITAL_WALLET -> "Please complete payment using your digital wallet."
        }
    }
}
