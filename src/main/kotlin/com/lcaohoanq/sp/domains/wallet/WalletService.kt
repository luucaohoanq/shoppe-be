package com.lcaohoanq.sp.domains.wallet

import com.lcaohoanq.sp.entities.*
import com.lcaohoanq.sp.repositories.UserRepository
import com.lcaohoanq.sp.repositories.WalletRepository
import com.lcaohoanq.sp.repositories.WalletTransactionRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class WalletService(
    private val walletRepository: WalletRepository,
    private val walletTransactionRepository: WalletTransactionRepository,
    private val userRepository: UserRepository
) : IWalletService {

    @Transactional(readOnly = true)
    override fun getWalletByWalletId(walletId: String): Wallet {
        return walletRepository.findByWalletId(walletId)
            .orElseThrow { EntityNotFoundException("Wallet not found with ID: $walletId") }
    }

    @Transactional(readOnly = true)
    override fun getWalletByUserId(userId: Long): Wallet {
        return walletRepository.findByUserId(userId)
            .orElseThrow { EntityNotFoundException("Wallet not found for user ID: $userId") }
    }

    @Transactional
    override fun createWallet(userId: Long, walletId: String, currency: String): Wallet {
        if (walletRepository.existsByWalletId(walletId)) {
            throw IllegalArgumentException("Wallet with ID $walletId already exists")
        }

        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User not found with ID: $userId") }

        val wallet = Wallet().apply {
            this.walletId = walletId
            this.currency = currency
            this.user = user
            this.active = true
            this.balance = BigDecimal.ZERO
        }

        return walletRepository.save(wallet)
    }

    @Transactional
    override fun deposit(walletId: String, amount: BigDecimal, description: String?): WalletTransaction {
        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Deposit amount must be positive")
        }

        val wallet = getWalletByWalletId(walletId)
        if (!wallet.active) {
            throw IllegalStateException("Cannot deposit to inactive wallet")
        }

        val transaction = WalletTransaction().apply {
            this.transactionId = UUID.randomUUID().toString()
            this.type = TransactionType.DEPOSIT
            this.amount = amount
            this.currency = wallet.currency
            this.description = description
            this.status = TransactionStatus.PENDING
        }

        wallet.addTransaction(transaction)
        walletRepository.save(wallet)
        
        // Complete the transaction
        transaction.complete()
        return walletTransactionRepository.save(transaction)
    }

    @Transactional
    override fun withdraw(walletId: String, amount: BigDecimal, description: String?): WalletTransaction {
        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Withdrawal amount must be positive")
        }

        val wallet = getWalletByWalletId(walletId)
        if (!wallet.active) {
            throw IllegalStateException("Cannot withdraw from inactive wallet")
        }

        if (!wallet.canWithdraw(amount)) {
            throw IllegalStateException("Insufficient funds for withdrawal")
        }

        val transaction = WalletTransaction().apply {
            this.transactionId = UUID.randomUUID().toString()
            this.type = TransactionType.WITHDRAWAL
            this.amount = amount
            this.currency = wallet.currency
            this.description = description
            this.status = TransactionStatus.PENDING
        }

        wallet.addTransaction(transaction)
        walletRepository.save(wallet)
        
        // Complete the transaction
        transaction.complete()
        return walletTransactionRepository.save(transaction)
    }

    @Transactional
    override fun processPayment(walletId: String, amount: BigDecimal, orderId: String): WalletTransaction {
        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Payment amount must be positive")
        }

        val wallet = getWalletByWalletId(walletId)
        if (!wallet.active) {
            throw IllegalStateException("Cannot process payment with inactive wallet")
        }

        if (!wallet.canWithdraw(amount)) {
            throw IllegalStateException("Insufficient funds for payment")
        }

        val transaction = WalletTransaction().apply {
            this.transactionId = UUID.randomUUID().toString()
            this.type = TransactionType.PAYMENT
            this.amount = amount
            this.currency = wallet.currency
            this.description = "Payment for order $orderId"
            this.referenceId = orderId
            this.status = TransactionStatus.PENDING
        }

        wallet.addTransaction(transaction)
        walletRepository.save(wallet)
        
        // Complete the transaction
        transaction.complete()
        return walletTransactionRepository.save(transaction)
    }

    @Transactional
    override fun processRefund(walletId: String, amount: BigDecimal, orderId: String): WalletTransaction {
        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Refund amount must be positive")
        }

        val wallet = getWalletByWalletId(walletId)
        if (!wallet.active) {
            throw IllegalStateException("Cannot process refund with inactive wallet")
        }

        val transaction = WalletTransaction().apply {
            this.transactionId = UUID.randomUUID().toString()
            this.type = TransactionType.REFUND
            this.amount = amount
            this.currency = wallet.currency
            this.description = "Refund for order $orderId"
            this.referenceId = orderId
            this.status = TransactionStatus.PENDING
        }

        wallet.addTransaction(transaction)
        walletRepository.save(wallet)
        
        // Complete the transaction
        transaction.complete()
        return walletTransactionRepository.save(transaction)
    }

    @Transactional(readOnly = true)
    override fun getTransactionHistory(walletId: String, pageable: Pageable): Page<WalletTransaction> {
        return walletTransactionRepository.findByWalletWalletId(walletId, pageable)
    }

    @Transactional(readOnly = true)
    override fun getTransactionById(transactionId: String): WalletTransaction {
        return walletTransactionRepository.findByTransactionId(transactionId)
            .orElseThrow { EntityNotFoundException("Transaction not found with ID: $transactionId") }
    }

    @Transactional(readOnly = true)
    override fun getBalance(walletId: String): BigDecimal {
        val wallet = getWalletByWalletId(walletId)
        return wallet.balance
    }

    @Transactional(readOnly = true)
    override fun getTransactionsByType(
        walletId: String,
        type: TransactionType,
        pageable: Pageable
    ): Page<WalletTransaction> {
        val wallet = getWalletByWalletId(walletId)
        return walletTransactionRepository.findByTypeAndStatus(type, TransactionStatus.COMPLETED, pageable)
            .filter { it.wallet?.id == wallet.id }
            .toList()
            .let { filteredList ->
                org.springframework.data.domain.PageImpl(
                    filteredList,
                    pageable,
                    filteredList.size.toLong()
                )
            }
    }
}