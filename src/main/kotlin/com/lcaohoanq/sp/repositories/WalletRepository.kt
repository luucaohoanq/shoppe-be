package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.entities.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface WalletRepository : JpaRepository<Wallet, Long> {
    fun findByWalletId(walletId: String): Optional<Wallet>
    fun findByUserId(userId: Long): Optional<Wallet>
    fun existsByWalletId(walletId: String): Boolean
}