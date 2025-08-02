package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.domains.settings.UserSettings
import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.enums.UserEnum
import com.lcaohoanq.sp.repositories.UserRepository
import com.lcaohoanq.sp.repositories.UserSettingsRepository
import org.springframework.security.crypto.password.PasswordEncoder

fun initUsers(
    userRepository: UserRepository,
    userSettingsRepository: UserSettingsRepository,
    passwordEncoder: PasswordEncoder
): List<User> {
    val hashedPassword = passwordEncoder.encode("1")

    // First, create users without UserSettings
    val users = listOf(
        User(
            email = "ad@gmail.com",
            hashedPassword = hashedPassword,
            name = "Admin User",
            role = UserEnum.Role.ADMIN,
            status = UserEnum.Status.VERIFIED,
            phone = "+84987654321",
            walletId = "wallet-ad@gmail.com",
            cartId = "cart-ad@gmail.com"
            // No UserSettings initially
        ),
        User(
            email = "cus@gmail.com",
            hashedPassword = hashedPassword,
            role = UserEnum.Role.CUSTOMER,
            status = UserEnum.Status.VERIFIED,
            phone = "+84123456789",
            walletId = "wallet-cus@gmail.com",
            cartId = "cart-cus@gmail.com"
            // No UserSettings initially
        ),
        User(
            email = "st@gmail.com",
            hashedPassword = hashedPassword,
            name = "STAFF User",
            role = UserEnum.Role.STAFF,
            status = UserEnum.Status.VERIFIED,
            phone = "+84123456789",
            walletId = "wallet-st@gmail.com",
            cartId = "cart-st@gmail.com"
            // No UserSettings initially
        ),
        User(
            email = "sp@gmail.com",
            hashedPassword = hashedPassword,
            name = "SHOP User",
            role = UserEnum.Role.SHOP,
            status = UserEnum.Status.VERIFIED,
            phone = "+84123456789",
            walletId = "wallet-sp@gmail.com",
            cartId = "cart-sp@gmail.com"
            // No UserSettings initially
        ),
        User(
            email = "sp2@gmail.com",
            hashedPassword = hashedPassword,
            name = "SHOP User 2",
            role = UserEnum.Role.SHOP,
            status = UserEnum.Status.DEACTIVATED,
            phone = "+84123456789",
            walletId = "wallet-sp2@gmail.com",
            cartId = "cart-sp2@gmail.com"
            // No UserSettings initially
        ),
        User(
            email = "manager@gmail.com",
            hashedPassword = hashedPassword,
            name = "MANAGER User",
            role = UserEnum.Role.MANAGER,
            status = UserEnum.Status.VERIFIED,
            phone = "+84123456789",
            walletId = "wallet-manager@gmail.com",
            cartId = "cart-manager@gmail.com"
            // No UserSettings initially
        ),
        User(
            email = "blocked@gmail.com",
            hashedPassword = hashedPassword,
            name = "Blocked User",
            role = UserEnum.Role.CUSTOMER,
            status = UserEnum.Status.BLOCKED,
            phone = "+84123456789",
            walletId = "wallet-blocked@gmail.com",
            cartId = "cart-blocked@gmail.com"
            // No UserSettings initially
        )
    )

    // Step 1: Save users first without UserSettings to get their IDs
    val savedUsers = userRepository.saveAll(users)

    // Step 2: Create and save UserSettings separately
    savedUsers.forEach { user ->
        val settings = UserSettings(userId = user.id)
        // Establish bidirectional relationship
        settings.user = user
        val savedSettings = userSettingsRepository.save(settings)

        // Update user with the saved settings reference
        user.userSettings = savedSettings
    }

    // Step 3: Save users again with the updated userSettings references
    return userRepository.saveAll(savedUsers)
}