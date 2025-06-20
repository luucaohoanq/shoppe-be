package com.lcaohoanq.sp.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "payments")
class Payment {

    @Id
    @SequenceGenerator(name = "payments_seq", sequenceName = "payments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payments_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var orderId: Int? = null
    var paymentMethodId: Int? = null
    var paid: Boolean? = null
    var paidAt: Int? = null
    var transactionId: Int? = null

}