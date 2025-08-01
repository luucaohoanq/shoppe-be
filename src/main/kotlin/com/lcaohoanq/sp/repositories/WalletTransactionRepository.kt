package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.entities.TransactionStatus
import com.lcaohoanq.sp.entities.TransactionType
import com.lcaohoanq.sp.entities.WalletTransaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional

@Repository
interface WalletTransactionRepository : JpaRepository<WalletTransaction, Long> {
    fun findByTransactionId(transactionId: String): Optional<WalletTransaction>
    fun findByWalletId(walletId: Long, pageable: Pageable): Page<WalletTransaction>
    fun findByWalletWalletId(walletId: String, pageable: Pageable): Page<WalletTransaction>
    fun findByTypeAndStatus(type: TransactionType, status: TransactionStatus, pageable: Pageable): Page<WalletTransaction>
    fun findByCreatedAtBetween(startDate: LocalDateTime, endDate: LocalDateTime, pageable: Pageable): Page<WalletTransaction>
    fun countByWalletIdAndTypeAndCreatedAtBetween(walletId: Long, type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): Long
}