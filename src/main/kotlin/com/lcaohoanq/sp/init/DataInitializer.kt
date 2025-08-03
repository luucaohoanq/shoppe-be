package com.lcaohoanq.sp.init

import com.lcaohoanq.sp.domains.currency.CurrencyRate
import com.lcaohoanq.sp.domains.headquarters.HeadquartersRepository
import com.lcaohoanq.sp.domains.thirdparty.ThirdPartyService
import com.lcaohoanq.sp.domains.wallet.WalletService
import com.lcaohoanq.sp.enums.Currency
import com.lcaohoanq.sp.mock.*
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
    private val thirdPartyService: ThirdPartyService,
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val userSettingsRepository: UserSettingsRepository,
    private val shippingMethodRepository: ShippingMethodRepository,
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val walletService: WalletService,
    private val adminSettingRepository: AdminSettingRepository,
    private val currencyRateRepository: CurrencyRateRepository,
    private val voucherRepository: VoucherRepository,
    private val productVoucherRepository: ProductVoucherRepository,
    private val notificationRepository: NotificationRepository,
    private val userDeviceTokenRepository: UserDeviceTokenRepository,
    private val headquartersRepository: HeadquartersRepository,

    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun initData(): CommandLineRunner {
        return CommandLineRunner {
            println("Starting data initialization...")

            if(headquartersRepository.count() == 0L) {
                println("Initializing headquarters...")
                // Initialize headquarters if needed
                initHeadquarters(headquartersRepository)
            } else {
                println("Headquarters already exist, skipping initialization")
            }

            if (voucherRepository.count() == 0L) {
                println("Initializing vouchers...")
                // Initialize vouchers if needed
                initComprehensiveVouchers(voucherRepository)
            } else {
                println("Vouchers already exist, skipping initialization")
            }

            if (currencyRateRepository.count() == 0L) {
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
            val products = if (productRepository.count() == 0L) {
                println("Initializing products...")
                initProducts(productRepository, categories)
                productRepository.findAll()
            } else {
                println("Products already exist, skipping initialization")
                productRepository.findAll()
            }

            // Initialize product-voucher relationships (depends on products and vouchers)
            if (productVoucherRepository.count() == 0L && products.isNotEmpty()) {
                println("Initializing product-voucher relationships...")
                val vouchers = voucherRepository.findAll()
                if (vouchers.isNotEmpty()) {
                    initProductVoucherRelationships(productVoucherRepository, products, vouchers)
                } else {
                    println("No vouchers available, skipping product-voucher relationships")
                }
            } else {
                println("Product-voucher relationships already exist, skipping initialization")
            }

            // Initialize users
            val users = if (userRepository.count() == 0L) {
                println("Initializing users...")
                initUsers(userRepository, userSettingsRepository, passwordEncoder)
            } else {
                println("Users already exist, skipping initialization")
                userRepository.findAll()
            }

            val userDeviceTokens = if(users.isNotEmpty() && userDeviceTokenRepository.count() == 0L) {
                println("Initializing user device tokens...")
                initUserDeviceTokens(users, userDeviceTokenRepository)
            } else {
                println("User device tokens already exist, skipping initialization")
            }

            // Initialize notifications
            val notifications = if (users.isNotEmpty() && notificationRepository.count() == 0L) {
                println("Initializing notifications...")
                initNotifications(users, notificationRepository)
            } else {
                println("No users found, skipping notification initialization")
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