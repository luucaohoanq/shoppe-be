package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.payment.Payment
import com.lcaohoanq.sp.domains.payment.PaymentPort

fun Payment.toPaymentResponse(): PaymentPort.PaymentResponse {
    return PaymentPort.PaymentResponse(
        id = this.id!!,
        orderId = this.orderId,
        amount = this.amount,
        paymentMethod = this.paymentMethod,
        status = this.status,
        transactionId = this.transactionId,
        paymentUrl = null, // This would be generated on demand
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.lastModifiedOn
    )
}
