package com.lcaohoanq.sp.init

import com.lcaohoanq.sp.domains.categories.Category
import com.lcaohoanq.sp.domains.product.Product
import com.lcaohoanq.sp.domains.settings.UserSettings
import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.domains.wallet.WalletService
import com.lcaohoanq.sp.entities.ShippingMethod
import com.lcaohoanq.sp.enums.UserEnum
import com.lcaohoanq.sp.repositories.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal

@Configuration
@Profile(value = ["dev", "test"]) // Only run in development environment
class DataInitializer {

    @Bean
    fun initData(
        categoryRepository: CategoryRepository,
        productRepository: ProductRepository,
        userRepository: UserRepository,
        shippingMethodRepository: ShippingMethodRepository,
        passwordEncoder: PasswordEncoder,
        paymentRepository: PaymentRepository,
        orderRepository: OrderRepository,
        cartRepository: CartRepository,
        walletService: WalletService
    ): CommandLineRunner {
        return CommandLineRunner {
            // Check if data already exists
            if (categoryRepository.count() > 0 || productRepository.count() > 0) {
                println("Database already has data, skipping initialization")
                return@CommandLineRunner
            }

            println("Initializing sample data...")

            // Create categories
            val categories = initCategories(categoryRepository)

            // Create products
            initProducts(productRepository, categories)

            // Create users
            initUsers(userRepository, passwordEncoder)

            // Create shipping methods
            initShippingMethods(shippingMethodRepository)

            // Create wallets
            initWallets(walletService, userRepository)

            println("Sample data initialization complete")
        }
    }

    private fun initCategories(categoryRepository: CategoryRepository): List<Category> {
        val categories = listOf(
            Category(
                name = "Electronics",
                description = "Electronic devices and accessories",
                slug = "electronics",
                imageUrl = "https://example.com/images/electronics.jpg",
                active = true
            ),
            Category(
                name = "Clothing",
                description = "Apparel and fashion items",
                slug = "clothing",
                imageUrl = "https://example.com/images/clothing.jpg",
                active = true
            ),
            Category(
                name = "Home & Kitchen",
                description = "Home appliances and kitchen essentials",
                slug = "home-kitchen",
                imageUrl = "https://example.com/images/home-kitchen.jpg",
                active = true
            ),
            Category(
                name = "Books",
                description = "Books, e-books, and publications",
                slug = "books",
                imageUrl = "https://example.com/images/books.jpg",
                active = true
            ),
            Category(
                name = "Beauty & Personal Care",
                description = "Beauty products and personal care items",
                slug = "beauty-personal-care",
                imageUrl = "https://example.com/images/beauty.jpg",
                active = true
            )
        )

        return categoryRepository.saveAll(categories)
    }

    private fun initProducts(productRepository: ProductRepository, categories: List<Category>) {
        val products = mutableListOf<Product>()

        // Electronics products
        val electronicsCategory = categories.find { it.name == "Electronics" }
        electronicsCategory?.let {
            products.add(
                Product(
                    name = "Smartphone X",
                    description = "Latest smartphone with advanced features",
                    price = 799.99,
                    stock = 50,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/smartphone-x.jpg",
                    sku = "ELEC-SP-001",
                    weight = 0.2,
                    dimensions = "15x7x1 cm",
                    featured = true,
                    active = true
                )
            )
            products.add(
                Product(
                    name = "Laptop Pro",
                    description = "High-performance laptop for professionals",
                    price = 1299.99,
                    stock = 30,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/laptop-pro.jpg",
                    sku = "ELEC-LP-002",
                    weight = 2.0,
                    dimensions = "35x25x2 cm",
                    featured = true,
                    active = true
                )
            )
            products.add(
                Product(
                    name = "Wireless Earbuds",
                    description = "Premium wireless earbuds with noise cancellation",
                    price = 149.99,
                    stock = 100,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/wireless-earbuds.jpg",
                    sku = "ELEC-WE-003",
                    weight = 0.05,
                    dimensions = "5x5x3 cm",
                    featured = false,
                    active = true
                )
            )
        }

        // Clothing products
        val clothingCategory = categories.find { it.name == "Clothing" }
        clothingCategory?.let {
            products.add(
                Product(
                    name = "Men's Casual Shirt",
                    description = "Comfortable cotton casual shirt for men",
                    price = 39.99,
                    stock = 200,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/mens-shirt.jpg",
                    sku = "CLOTH-MS-001",
                    weight = 0.3,
                    dimensions = "30x20x2 cm",
                    featured = false,
                    active = true
                )
            )
            products.add(
                Product(
                    name = "Women's Dress",
                    description = "Elegant dress for women",
                    price = 59.99,
                    stock = 150,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/womens-dress.jpg",
                    sku = "CLOTH-WD-002",
                    weight = 0.4,
                    dimensions = "40x30x2 cm",
                    featured = true,
                    active = true
                )
            )
        }

        // Home & Kitchen products
        val homeCategory = categories.find { it.name == "Home & Kitchen" }
        homeCategory?.let {
            products.add(
                Product(
                    name = "Coffee Maker",
                    description = "Automatic coffee maker with timer",
                    price = 89.99,
                    stock = 75,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/coffee-maker.jpg",
                    sku = "HOME-CM-001",
                    weight = 3.0,
                    dimensions = "25x20x30 cm",
                    featured = false,
                    active = true
                )
            )
            products.add(
                Product(
                    name = "Blender",
                    description = "High-speed blender for smoothies and more",
                    price = 69.99,
                    stock = 60,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/blender.jpg",
                    sku = "HOME-BL-002",
                    weight = 2.5,
                    dimensions = "20x15x35 cm",
                    featured = false,
                    active = true
                )
            )
        }

        // Books products
        val booksCategory = categories.find { it.name == "Books" }
        booksCategory?.let {
            products.add(
                Product(
                    name = "The Great Novel",
                    description = "Bestselling fiction novel",
                    price = 14.99,
                    stock = 300,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/great-novel.jpg",
                    sku = "BOOK-GN-001",
                    weight = 0.5,
                    dimensions = "20x15x3 cm",
                    featured = false,
                    active = true
                )
            )
            products.add(
                Product(
                    name = "Cooking Masterclass",
                    description = "Comprehensive cookbook for all skill levels",
                    price = 24.99,
                    stock = 150,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/cookbook.jpg",
                    sku = "BOOK-CM-002",
                    weight = 0.8,
                    dimensions = "25x20x2 cm",
                    featured = true,
                    active = true
                )
            )
        }

        // Beauty products
        val beautyCategory = categories.find { it.name == "Beauty & Personal Care" }
        beautyCategory?.let {
            products.add(
                Product(
                    name = "Facial Cleanser",
                    description = "Gentle facial cleanser for all skin types",
                    price = 19.99,
                    stock = 200,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/facial-cleanser.jpg",
                    sku = "BEAUTY-FC-001",
                    weight = 0.3,
                    dimensions = "10x5x15 cm",
                    featured = false,
                    active = true
                )
            )
            products.add(
                Product(
                    name = "Luxury Perfume",
                    description = "Premium fragrance for special occasions",
                    price = 79.99,
                    stock = 50,
                    categoryId = it.id!!,
                    imageUrl = "https://example.com/images/perfume.jpg",
                    sku = "BEAUTY-LP-002",
                    weight = 0.2,
                    dimensions = "8x8x15 cm",
                    featured = true,
                    active = true
                )
            )
        }

        productRepository.saveAll(products)
    }

    private fun initUsers(userRepository: UserRepository, passwordEncoder: PasswordEncoder) {
        val users = listOf(
            User(
                email = "admin@example.com",
                hashedPassword = passwordEncoder.encode("admin123"),
                name = "Admin User",
                role = UserEnum.Role.ADMIN,
                isActive = true,
                phone = "+84987654321",
                walletId = "wallet-admin@example.com",
                preferredLanguage = "en",
                preferredCurrency = "USD",
                cartId = "cart-admin@example.com",
                userSettings = UserSettings(
                    userId = null,
                    twoFaEnabled = false,
                    preferredLanguage = "en",
                    darkMode = false,
                    loginAlerts = true,
                    requestDisableAccount = false,
                )
            ),
            User(
                email = "user@example.com",
                hashedPassword = passwordEncoder.encode("user123"),
                name = "Regular User",
                role = UserEnum.Role.CUSTOMER,
                isActive = true,
                phone = "+84123456789",
                walletId = "wallet-user@example.com",
                preferredLanguage = "en",
                preferredCurrency = "USD",
                cartId = "cart-user@example.com",
                userSettings = UserSettings(
                    userId = null,
                    twoFaEnabled = false,
                    preferredLanguage = "en",
                    darkMode = true,
                    loginAlerts = true,
                    requestDisableAccount = false,
                )
            )
        )

        userRepository.saveAll(users)
    }

    private fun initShippingMethods(shippingMethodRepository: ShippingMethodRepository) {
        val shippingMethods = listOf(
            ShippingMethod().apply {
                name = "Standard Shipping"
                cost = 5.99
                estimatedDays = 5
                active = true
            },
            ShippingMethod().apply {
                name = "Express Shipping"
                cost = 15.99
                estimatedDays = 2
                active = true
            },
            ShippingMethod().apply {
                name = "Next Day Delivery"
                cost = 25.99
                estimatedDays = 1
                active = true
            }
        )

        shippingMethodRepository.saveAll(shippingMethods)
    }

    private fun initWallets(walletService: WalletService, userRepository: UserRepository) {
        // Find users
        val adminUser = userRepository.findByEmail("admin@example.com").orElse(null)
        val regularUser = userRepository.findByEmail("user@example.com").orElse(null)

        // Create wallets for users
        adminUser?.let {
            val wallet = walletService.createWallet(it.id!!, it.walletId, "USD")
            // Add some initial funds to admin wallet
            walletService.deposit(wallet.walletId, BigDecimal(1000.0), "Initial deposit for admin")
        }

        regularUser?.let {
            val wallet = walletService.createWallet(it.id!!, it.walletId, "USD")
            // Add some initial funds to regular user wallet
            walletService.deposit(wallet.walletId, BigDecimal(500.0), "Initial deposit for user")
        }
    }
}