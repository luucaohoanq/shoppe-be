package com.lcaohoanq.sp.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.lcaohoanq.sp.bases.BaseEntity
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "wallet_transactions")
class WalletTransaction : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    
    @Column(nullable = false)
    var transactionId: String = ""
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: TransactionType = TransactionType.DEPOSIT
    
    @Column(nullable = false)
    var amount: BigDecimal = BigDecimal.ZERO
    
    @Column(nullable = false)
    var currency: String = "USD"
    
    @Column(nullable = true)
    var description: String? = null
    
    @Column(nullable = true)
    var referenceId: String? = null
    
    @Column(nullable = false)
    var status: TransactionStatus = TransactionStatus.PENDING
    
    @Column(nullable = true)
    var completedAt: LocalDateTime? = null
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    var wallet: Wallet? = null
    
    fun complete() {
        status = TransactionStatus.COMPLETED
        completedAt = LocalDateTime.now()
    }
    
    fun fail(reason: String) {
        status = TransactionStatus.FAILED
        description = reason
    }
}

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED
}