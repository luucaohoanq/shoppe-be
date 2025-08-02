package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.domains.wallet.WalletService
import com.lcaohoanq.sp.repositories.UserRepository
import java.math.BigDecimal

fun initWallets(walletService: WalletService, userRepository: UserRepository) {
    // Find users with corrected email addresses
    val adminUser = userRepository.findByEmail("ad@gmail.com").orElse(null)
    val regularUser = userRepository.findByEmail("cus@gmail.com").orElse(null)
    val staffUser = userRepository.findByEmail("st@gmail.com").orElse(null)
    val shopUser = userRepository.findByEmail("sp@gmail.com").orElse(null)
    val shopUser2 = userRepository.findByEmail("sp2@gmail.com").orElse(null)
    val managerUser = userRepository.findByEmail("manager@gmail.com").orElse(null)

    // Create wallets for users
    adminUser?.let {
        try {
            val wallet = walletService.createWallet(it.id!!, it.walletId, "USD")
            // Add some initial funds to admin wallet
            walletService.deposit(
                wallet.walletId,
                BigDecimal(1000.0),
                "Initial deposit for admin"
            )
            println("Created wallet for admin user")
        } catch (e: Exception) {
            println("Wallet for admin user may already exist: ${e.message}")
        }
    }

    regularUser?.let {
        try {
            val wallet = walletService.createWallet(it.id!!, it.walletId, "USD")
            // Add some initial funds to regular user wallet
            walletService.deposit(
                wallet.walletId,
                BigDecimal(500.0),
                "Initial deposit for user"
            )
            println("Created wallet for regular user")
        } catch (e: Exception) {
            println("Wallet for regular user may already exist: ${e.message}")
        }
    }

    staffUser?.let {
        try {
            val wallet = walletService.createWallet(it.id!!, it.walletId, "USD")
            // Add some initial funds to staff user wallet
            walletService.deposit(
                wallet.walletId,
                BigDecimal(300.0),
                "Initial deposit for staff user"
            )
            println("Created wallet for staff user")
        } catch (e: Exception) {
            println("Wallet for staff user may already exist: ${e.message}")
        }
    }

    shopUser?.let {
        try {
            val wallet = walletService.createWallet(it.id!!, it.walletId, "USD")
            // Add some initial funds to shop user wallet
            walletService.deposit(
                wallet.walletId,
                BigDecimal(200.0),
                "Initial deposit for shop user"
            )
            println("Created wallet for shop user")
        } catch (e: Exception) {
            println("Wallet for shop user may already exist: ${e.message}")
        }
    }

    shopUser2?.let {
        try {
            val wallet = walletService.createWallet(it.id!!, it.walletId, "USD")
            // Add some initial funds to shop user 2 wallet
            walletService.deposit(
                wallet.walletId,
                BigDecimal(150.0),
                "Initial deposit for shop user 2"
            )
            println("Created wallet for shop user 2")
        } catch (e: Exception) {
            println("Wallet for shop user 2 may already exist: ${e.message}")
        }
    }

    managerUser?.let {
        try {
            val wallet = walletService.createWallet(it.id!!, it.walletId, "USD")
            // Add some initial funds to manager user wallet
            walletService.deposit(
                wallet.walletId,
                BigDecimal(800.0),
                "Initial deposit for manager user"
            )
            println("Created wallet for manager user")
        } catch (e: Exception) {
            println("Wallet for manager user may already exist: ${e.message}")
        }
    }


}