package com.lcaohoanq.sp.domains.wallet

import com.lcaohoanq.sp.entities.TransactionType
import com.lcaohoanq.sp.entities.Wallet
import com.lcaohoanq.sp.entities.WalletTransaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.math.BigDecimal

interface IWalletService {
    fun getWalletByWalletId(walletId: String): Wallet
    fun getWalletByUserId(userId: Long): Wallet
    fun createWallet(userId: Long, walletId: String, currency: String = "USD"): Wallet
    fun deposit(walletId: String, amount: BigDecimal, description: String? = null): WalletTransaction
    fun withdraw(walletId: String, amount: BigDecimal, description: String? = null): WalletTransaction
    fun processPayment(walletId: String, amount: BigDecimal, orderId: String): WalletTransaction
    fun processRefund(walletId: String, amount: BigDecimal, orderId: String): WalletTransaction
    fun getTransactionHistory(walletId: String, pageable: Pageable): Page<WalletTransaction>
    fun getTransactionById(transactionId: String): WalletTransaction
    fun getBalance(walletId: String): BigDecimal
    fun getTransactionsByType(walletId: String, type: TransactionType, pageable: Pageable): Page<WalletTransaction>
}