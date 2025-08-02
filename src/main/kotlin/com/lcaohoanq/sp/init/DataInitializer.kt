package com.lcaohoanq.sp.init

import com.lcaohoanq.sp.domains.categories.Category
import com.lcaohoanq.sp.domains.currency.CurrencyRate
import com.lcaohoanq.sp.domains.product.Product
import com.lcaohoanq.sp.domains.settings.UserSettings
import com.lcaohoanq.sp.domains.thirdparty.ThirdPartyService
import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.domains.wallet.WalletService
import com.lcaohoanq.sp.entities.SystemSetting
import com.lcaohoanq.sp.entities.ShippingMethod
import com.lcaohoanq.sp.enums.Currency
import com.lcaohoanq.sp.enums.UserEnum
import com.lcaohoanq.sp.repositories.*
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal
import java.math.RoundingMode

@Configuration
@Profile(value = ["dev", "test"]) // Only run in development environment
class DataInitializer(
    private val thirdPartyService: ThirdPartyService
) {

    @Bean
    fun initData(
        categoryRepository: CategoryRepository,
        productRepository: ProductRepository,
        userRepository: UserRepository,
        userSettingsRepository: UserSettingsRepository,
        shippingMethodRepository: ShippingMethodRepository,
        passwordEncoder: PasswordEncoder,
        paymentRepository: PaymentRepository,
        orderRepository: OrderRepository,
        cartRepository: CartRepository,
        walletService: WalletService,
        adminSettingRepository: AdminSettingRepository,
        currencyRateRepository: CurrencyRateRepository
    ): CommandLineRunner {
        return CommandLineRunner {
            println("Starting data initialization...")

            val currencyRateCount = if (currencyRateRepository.count() == 0L) {
                println("Initializing currency rates...")
                // Initialize currency rates if needed
                initCurrencyRates(currencyRateRepository)
            } else {
                println("Currency rates already exist, skipping initialization")
            }

            // Initialize categories first (products depend on categories)
            val categories = if (categoryRepository.count() == 0L) {
                println("Initializing categories...")
                initCategories(categoryRepository)
            } else {
                println("Categories already exist, skipping initialization")
                categoryRepository.findAll()
            }

            // Initialize products (depends on categories)
            if (productRepository.count() == 0L) {
                println("Initializing products...")
                initProducts(productRepository, categories)
            } else {
                println("Products already exist, skipping initialization")
            }

            // Initialize users
            val users = if (userRepository.count() == 0L) {
                println("Initializing users...")
                initUsers(userRepository, userSettingsRepository, passwordEncoder)
            } else {
                println("Users already exist, skipping initialization")
                userRepository.findAll()
            }

            // Initialize shipping methods
            if (shippingMethodRepository.count() == 0L) {
                println("Initializing shipping methods...")
                initShippingMethods(shippingMethodRepository)
            } else {
                println("Shipping methods already exist, skipping initialization")
            }

            // Initialize wallets (depends on users)
            if (users.isNotEmpty()) {
                println("Initializing wallets...")
                initWallets(walletService, userRepository)
            }

            // Skip payment, order, and cart initialization if they already have data
            if (paymentRepository.count() > 0L) {
                println("Payments already exist, skipping wallet funding")
            }

            if (orderRepository.count() > 0L) {
                println("Orders already exist")
            }

            if (cartRepository.count() > 0L) {
                println("Carts already exist")
            }

            // Initialize admin settings
            if (adminSettingRepository.count() == 0L) {
                println("Initializing admin settings...")
                initAdminSettings(adminSettingRepository)
            } else {
                println("Admin settings already exist, skipping initialization")
            }

            println("Data initialization complete")
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
                    rating = 5.0,
                    stock = 50,
                    imageUrl = "https://example.com/images/smartphone-x.jpg",
                    images = listOf(
                        "https://api-ecom.duthanhduoc.com/images/08b79b1d-169d-4de1-85a2-4e5e8ff535b7.jpg",
                        "https://api-ecom.duthanhduoc.com/images/182d6e25-65fa-4abe-b822-70d87550bf4e.jpg",
                        "https://api-ecom.duthanhduoc.com/images/827e675d-e553-497e-9b15-d2df5fc7192d.jpg",
                        "https://api-ecom.duthanhduoc.com/images/b6425e3f-3cc3-4696-94f7-5053afca2c71.jpg",
                        "https://api-ecom.duthanhduoc.com/images/4d80b312-e605-4508-ab80-14dd75f6d23d.jpg",
                        "https://api-ecom.duthanhduoc.com/images/9e628716-0b94-44d8-850c-e96adc4b1c8f.jpg",
                        "https://api-ecom.duthanhduoc.com/images/20a1a8e5-1b49-4854-a221-0f96130b5fd8.jpg",
                    ),
                    sku = "ELEC-SP-001",
                    weight = 0.2,
                    dimensions = "15x7x1 cm",
                    featured = true,
                )
            )
            products.add(
                Product(
                    name = "Laptop Pro",
                    description = "High-performance laptop for professionals",
                    price = 1299.99,
                    stock = 30,
                    imageUrl = "https://example.com/images/laptop-pro.jpg",
                    sku = "ELEC-LP-002",
                    weight = 2.0,
                    dimensions = "35x25x2 cm",
                    featured = true,
                )
            )
            products.add(
                Product(
                    name = "Wireless Earbuds",
                    description = "Premium wireless earbuds with noise cancellation",
                    price = 149.99,
                    stock = 100,
                    imageUrl = "https://example.com/images/wireless-earbuds.jpg",
                    sku = "ELEC-WE-003",
                    weight = 0.05,
                    dimensions = "5x5x3 cm",
                    featured = false,
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
                    imageUrl = "https://example.com/images/mens-shirt.jpg",
                    sku = "CLOTH-MS-001",
                    weight = 0.3,
                    dimensions = "30x20x2 cm",
                    featured = false,
                )
            )
            products.add(
                Product(
                    name = "Women's Dress",
                    description = "Elegant dress for women",
                    price = 59.99,
                    stock = 150,
                    imageUrl = "https://example.com/images/womens-dress.jpg",
                    sku = "CLOTH-WD-002",
                    weight = 0.4,
                    dimensions = "40x30x2 cm",
                    featured = true,
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
                    imageUrl = "https://example.com/images/coffee-maker.jpg",
                    sku = "HOME-CM-001",
                    weight = 3.0,
                    dimensions = "25x20x30 cm",
                    featured = false,
                )
            )
            products.add(
                Product(
                    name = "Blender",
                    description = "High-speed blender for smoothies and more",
                    price = 69.99,
                    stock = 60,
                    imageUrl = "https://example.com/images/blender.jpg",
                    sku = "HOME-BL-002",
                    weight = 2.5,
                    dimensions = "20x15x35 cm",
                    featured = false,
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
                    imageUrl = "https://example.com/images/great-novel.jpg",
                    sku = "BOOK-GN-001",
                    weight = 0.5,
                    dimensions = "20x15x3 cm",
                    featured = false,
                )
            )
            products.add(
                Product(
                    name = "Cooking Masterclass",
                    description = "Comprehensive cookbook for all skill levels",
                    price = 24.99,
                    stock = 150,
                    imageUrl = "https://example.com/images/cookbook.jpg",
                    sku = "BOOK-CM-002",
                    weight = 0.8,
                    dimensions = "25x20x2 cm",
                    featured = true,
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
                    imageUrl = "https://example.com/images/facial-cleanser.jpg",
                    sku = "BEAUTY-FC-001",
                    weight = 0.3,
                    dimensions = "10x5x15 cm",
                    featured = false,
                )
            )
            products.add(
                Product(
                    name = "Luxury Perfume",
                    description = "Premium fragrance for special occasions",
                    price = 79.99,
                    stock = 50,
                    imageUrl = "https://example.com/images/perfume.jpg",
                    sku = "BEAUTY-LP-002",
                    weight = 0.2,
                    dimensions = "8x8x15 cm",
                    featured = true,
                )
            )
        }

        productRepository.saveAll(products)
    }

    private fun initUsers(
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

    private fun initAdminSettings(adminSettingRepository: AdminSettingRepository): List<SystemSetting> {
        val systemSettings = listOf(
            SystemSetting().apply {
                settingKey = "site.name"
                settingValue = "Shoppe"
                description = "Name of the e-commerce site"
            },
            SystemSetting().apply {
                settingKey = "site.description"
                settingValue = "An e-commerce platform"
                description = "Description of the e-commerce site"
            },
            SystemSetting().apply {
                settingKey = "site.contact.email"
                settingValue = "contact@shoppe.com"
                description = "Contact email for the site"
            },
            SystemSetting().apply {
                settingKey = "site.contact.phone"
                settingValue = "+84123456789"
                description = "Contact phone number for the site"
            },
            SystemSetting().apply {
                settingKey = "payment.currency"
                settingValue = "USD"
                description = "Default currency for payments"
            },
            SystemSetting().apply {
                settingKey = "payment.methods"
                settingValue = "Credit Card,PayPal,Bank Transfer"
                description = "Available payment methods"
            },
            SystemSetting().apply {
                settingKey = "order.auto_confirm"
                settingValue = "false"
                description = "Automatically confirm orders after payment"
            },
            SystemSetting().apply {
                settingKey = "user.registration.enabled"
                settingValue = "true"
                description = "Enable user registration"
            },
            SystemSetting().apply {
                settingKey = "user.verification.required"
                settingValue = "true"
                description = "Require email verification for new users"
            },
            SystemSetting().apply {
                settingKey = "maintenance.mode"
                settingValue = "false"
                description = "Enable maintenance mode for the site"
            },
            SystemSetting().apply {
                settingKey = "file.max_upload_size"
                settingValue = "10485760" // 10 MB in bytes
                description = "Maximum file upload size in bytes"
            }
        )

        return adminSettingRepository.saveAll(systemSettings)
    }

    private fun initWallets(walletService: WalletService, userRepository: UserRepository) {
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

    //App nhỏ, dashboard admin, không cần truy vấn phức tạp	Cách 2 (tính toán khi cần)
    //App tài chính, API cung cấp tỉ giá đa dạng, cần truy xuất nhanh	Cách 1 (lưu 2 chiều)
    fun initCurrencyRates(currencyRateRepository: CurrencyRateRepository) {
        val baseCurrency = Currency.USD
        val response = runBlocking { thirdPartyService.getBaseCurrencyRate(baseCurrency) }

        val supportedCurrencies =
            listOf(Currency.USD, Currency.VND, Currency.JPY) // hoặc lấy từ enum

        val rates = mutableListOf<CurrencyRate>()

        // Lưu USD → others
        for (toCurrency in supportedCurrencies) {
            if (toCurrency != baseCurrency) {
                val rate = response.rates[toCurrency.name] ?: continue
                rates.add(
                    CurrencyRate(
                        fromCurrency = baseCurrency,
                        toCurrency = toCurrency,
                        rate = BigDecimal.valueOf(rate)
                    )
                )
            }
        }

        // Lưu others → USD
        for (fromCurrency in supportedCurrencies) {
            if (fromCurrency != baseCurrency) {
                val rate = response.rates[fromCurrency.name] ?: continue
                val reverseRate = BigDecimal.ONE.divide(
                    BigDecimal.valueOf(rate),
                    6,
                    RoundingMode.HALF_UP
                )

                println("Reverse rate from $fromCurrency to $baseCurrency: $reverseRate")

                rates.add(
                    CurrencyRate(
                        fromCurrency = fromCurrency,
                        toCurrency = baseCurrency,
                        rate = reverseRate
                    )
                )
            }
        }

        currencyRateRepository.saveAll(rates)
    }

}