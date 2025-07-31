package com.lcaohoanq.sp.domains.payment

import com.lcaohoanq.sp.apis.MyApiResponseV2
import com.lcaohoanq.sp.apis.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment Management", description = "APIs for payment operations")
class PaymentController(
    private val paymentService: IPaymentService
) {
    
    private val log = KotlinLogging.logger {}

    @PostMapping
    @Operation(summary = "Create a new payment", description = "Create a new payment for an order")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    fun createPayment(
        @Valid @RequestBody request: PaymentPort.CreatePaymentRequest
    ): ResponseEntity<MyApiResponseV2<PaymentPort.PaymentResponse>> {
        log.info { "Creating payment for order: ${request.orderId}" }
        
        val payment = paymentService.createPayment(request)
        
        return MyApiResponseV2.created(
                data = payment
        )
    }

    @PostMapping("/{paymentId}/process")
    @Operation(summary = "Process payment", description = "Process a pending payment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    fun processPayment(
        @Parameter(description = "Payment ID") @PathVariable paymentId: Long
    ): ResponseEntity<MyApiResponseV2<PaymentPort.PaymentResponse>> {
        log.info { "Processing payment: $paymentId" }
        
        val payment = paymentService.processPayment(paymentId)
        
        return MyApiResponseV2.success(
                data = payment
            )

    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment by ID", description = "Retrieve payment details by payment ID")
    fun getPaymentById(
        @Parameter(description = "Payment ID") @PathVariable paymentId: Long
    ): ResponseEntity<MyApiResponseV2<PaymentPort.PaymentResponse>> {
        log.info { "Getting payment: $paymentId" }
        
        val payment = paymentService.getPaymentById(paymentId)
        
        return MyApiResponseV2.success(
                data = payment

        )
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID", description = "Retrieve payment details by order ID")
    fun getPaymentByOrderId(
        @Parameter(description = "Order ID") @PathVariable orderId: Long
    ): ResponseEntity<MyApiResponseV2<PaymentPort.PaymentResponse?>> {
        log.info { "Getting payment for order: $orderId" }
        
        val payment = paymentService.getPaymentByOrderId(orderId)
        
        return MyApiResponseV2.success(
                data = payment

        )
    }

    @PutMapping("/{paymentId}/status")
    @Operation(summary = "Update payment status", description = "Update payment status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    fun updatePaymentStatus(
        @Parameter(description = "Payment ID") @PathVariable paymentId: Long,
        @Valid @RequestBody request: PaymentPort.UpdatePaymentStatusRequest
    ): ResponseEntity<MyApiResponseV2<PaymentPort.PaymentResponse>> {
        log.info { "Updating payment status for payment: $paymentId to ${request.status}" }
        
        val payment = paymentService.updatePaymentStatus(paymentId, request)
        
        return MyApiResponseV2.success(
                data = payment

        )
    }

    @PostMapping("/{paymentId}/refund")
    @Operation(summary = "Refund payment", description = "Refund a completed payment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    fun refundPayment(
        @Parameter(description = "Payment ID") @PathVariable paymentId: Long,
        @RequestParam(required = false) reason: String?
    ): ResponseEntity<MyApiResponseV2<PaymentPort.PaymentResponse>> {
        log.info { "Refunding payment: $paymentId" }
        
        val payment = paymentService.refundPayment(paymentId, reason)
        
        return MyApiResponseV2.success(
                data = payment

        )
    }

    @PostMapping("/{paymentId}/cancel")
    @Operation(summary = "Cancel payment", description = "Cancel a pending payment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    fun cancelPayment(
        @Parameter(description = "Payment ID") @PathVariable paymentId: Long
    ): ResponseEntity<MyApiResponseV2<PaymentPort.PaymentResponse>> {
        log.info { "Cancelling payment: $paymentId" }
        
        val payment = paymentService.cancelPayment(paymentId)
        
        return MyApiResponseV2.success(
                data = payment

        )
    }

    @GetMapping("/{paymentId}/validate")
    @Operation(summary = "Validate payment", description = "Validate if payment is in a valid state")
    fun validatePayment(
        @Parameter(description = "Payment ID") @PathVariable paymentId: Long
    ): ResponseEntity<MyApiResponseV2<Boolean>> {
        log.info { "Validating payment: $paymentId" }
        
        val isValid = paymentService.validatePayment(paymentId)
        
        return MyApiResponseV2.success(
                data = isValid

        )
    }

    @PostMapping("/callback")
    @Operation(summary = "Payment callback", description = "Handle payment gateway callbacks")
    fun handlePaymentCallback(
        @RequestParam transactionId: String,
        @RequestParam status: Payment.PaymentStatus
    ): ResponseEntity<MyApiResponseV2<Boolean>> {
        log.info { "Handling payment callback for transaction: $transactionId, status: $status" }
        
        val success = paymentService.handlePaymentCallback(transactionId, status)
        
        return MyApiResponseV2.success(data = success)
    }

    // Admin endpoints
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get all payments (Admin)", description = "Retrieve all payments for admin/manager")
    fun getAllPayments(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PageResponse<PaymentPort.PaymentResponse>> {
        log.info { "Getting all payments (admin)" }
        
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        val payments = paymentService.getAllPayments(pageable)
        
        return ResponseEntity.ok(payments)
    }

    @PostMapping("/admin/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Search payments (Admin)", description = "Search payments with filters for admin/manager")
    fun searchPayments(
        @Valid @RequestBody request: PaymentPort.PaymentSearchRequest,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PageResponse<PaymentPort.PaymentResponse>> {
        log.info { "Searching payments (admin)" }
        
        val pageable = PageRequest.of(page, size, Sort.by("createdAt").descending())
        val payments = paymentService.searchPayments(request, pageable)
        
        return ResponseEntity.ok(payments)
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get payment statistics (Admin)", description = "Retrieve payment statistics for admin/manager")
    fun getPaymentStats(): ResponseEntity<MyApiResponseV2<PaymentPort.PaymentStatsResponse>> {
        log.info { "Getting payment statistics (admin)" }
        
        val stats = paymentService.getPaymentStats()
        
        return MyApiResponseV2.success(data = stats)
    }

    @GetMapping("/admin/failed")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get failed payments (Admin)", description = "Retrieve failed payments for admin/manager")
    fun getFailedPayments(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PageResponse<PaymentPort.PaymentResponse>> {
        log.info { "Getting failed payments (admin)" }
        
        val pageable = PageRequest.of(page, size)
        val payments = paymentService.getFailedPayments(pageable)
        
        return ResponseEntity.ok(payments)
    }

    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Get pending payments (Admin)", description = "Retrieve pending payments for admin/manager")
    fun getPendingPayments(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PageResponse<PaymentPort.PaymentResponse>> {
        log.info { "Getting pending payments (admin)" }
        
        val pageable = PageRequest.of(page, size)
        val payments = paymentService.getPendingPayments(pageable)
        
        return ResponseEntity.ok(payments)
    }
}
