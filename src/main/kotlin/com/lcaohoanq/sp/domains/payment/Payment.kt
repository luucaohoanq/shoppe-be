package com.lcaohoanq.sp.domains.payment

import com.lcaohoanq.sp.bases.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "payments")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    var orderId: Long = 0L,
    var paymentMethodId: Int? = null,
    var paid: Boolean? = null,
    var paidAt: Int? = null,
    var transactionId: String? = null,
    var amount: Double = 0.0,
    var status: PaymentStatus = PaymentStatus.PENDING,
    var paymentMethod: PaymentMethod = PaymentMethod.CREDIT_CARD,
    var description: String = ""
) : BaseEntity() {

    enum class PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED,
        CANCELLED
    }

    //enum PaymentMethod
    enum class PaymentMethod {
        CREDIT_CARD,
        PAYPAL,
        BANK_TRANSFER,
        CASH_ON_DELIVERY,
        DIGITAL_WALLET
    }

}