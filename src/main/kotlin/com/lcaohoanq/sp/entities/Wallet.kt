package com.lcaohoanq.sp.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import BaseEntity
import com.lcaohoanq.sp.domains.user.User
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "wallets")
class Wallet : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    
    @Column(nullable = false, unique = true)
    var walletId: String = ""
    
    @Column(nullable = false)
    var balance: BigDecimal = BigDecimal.ZERO
    
    @Column(nullable = false)
    var currency: String = "USD"
    
    @Column(nullable = false)
    var active: Boolean = true
    
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User? = null
    
    @OneToMany(mappedBy = "wallet", cascade = [CascadeType.ALL], orphanRemoval = true)
    var transactions: MutableList<WalletTransaction> = mutableListOf()
    
    fun addTransaction(transaction: WalletTransaction) {
        transactions.add(transaction)
        transaction.wallet = this
        
        // Update balance based on transaction type
        when (transaction.type) {
            TransactionType.DEPOSIT -> balance = balance.add(transaction.amount)
            TransactionType.WITHDRAWAL -> balance = balance.subtract(transaction.amount)
            TransactionType.PAYMENT -> balance = balance.subtract(transaction.amount)
            TransactionType.REFUND -> balance = balance.add(transaction.amount)
        }
    }
    
    fun canWithdraw(amount: BigDecimal): Boolean {
        return balance.compareTo(amount) >= 0 && active
    }
}

enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    PAYMENT,
    REFUND
}