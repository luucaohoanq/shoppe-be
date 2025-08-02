package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.domains.discount.Voucher
import com.lcaohoanq.sp.domains.discount.VoucherCondition
import com.lcaohoanq.sp.enums.DiscountType
import com.lcaohoanq.sp.repositories.VoucherRepository
import java.time.LocalDateTime

/*
Voucher Categories:
Welcome & New User Vouchers (3)

Various discount types for new customers
Different minimum purchase amounts and limits

Seasonal Vouchers (4)

Spring, Summer, Fall, and Winter themed discounts
Varying discount percentages and conditions

Holiday Special Vouchers (5)

Black Friday, Cyber Monday, New Year, Valentine's, Easter
High-value discounts with appropriate limitations

Amount-based Vouchers (3)

Fixed dollar amounts off purchases
Different minimum purchase thresholds

Free Shipping Vouchers (3)

Various minimum purchase amounts
Including express shipping options

Buy X Get Y Vouchers (3)

BOGO, Buy 2 Get 1, Buy 3 Get 2
With appropriate minimum purchase amounts

Free Gift Vouchers (3)

Different gift product IDs
Various minimum purchase thresholds

Category-Specific Vouchers (4)

Electronics, Fashion, Home & Garden, Books
Targeted discounts for specific product categories

Product-Specific Vouchers (2)

iPhone and laptop specific discounts
High-value items with substantial discounts

Limited Time Flash Sales (2)

Very short duration vouchers (30 minutes, 1 hour)
High discount values with limited usage

Loyalty & VIP Vouchers (2)

Exclusive discounts for premium customers
Higher discount values with extended validity

Test Cases (3)

Expired voucher
Inactive voucher
Fully used voucher (for testing edge cases)

Special Purpose Vouchers (3)

International shipping
Bulk purchase incentives
Wholesale discounts
*/

fun initComprehensiveVouchers(voucherRepository: VoucherRepository) {
    val vouchers = listOf(
        // Welcome & New User Vouchers
        Voucher(
            code = "WELCOME10",
            description = "Lượt sử dụng có hạn. Nhanh tay kẻo lỡ bạn nhé!Giảm 6% Đơn Tối Thiểu ₫99k Giảm tối đa ₫15k",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 10.0,
            condition = VoucherCondition(
                minPurchaseAmount = 50.0,
                maxDiscountAmount = 20.0,
            ),
            usageLimit = 100,
            usedCount = 15,
            active = true,
            validFrom = LocalDateTime.now().minusDays(5),
            validUntil = LocalDateTime.now().plusDays(25)
        ),
        Voucher(
            code = "FIRSTORDER15",
            description = "First order special discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 15.0,
            condition = VoucherCondition(
                minPurchaseAmount = 30.0,
                maxDiscountAmount = 25.0,
            ),
            usageLimit = 500,
            usedCount = 127,
            active = true,
            validFrom = LocalDateTime.now().minusDays(10),
            validUntil = LocalDateTime.now().plusDays(20)
        ),
        Voucher(
            code = "NEWBIE5OFF",
            description = "New customer $5 off",
            discountType = DiscountType.AMOUNT,
            discountValue = 5.0,
            condition = VoucherCondition(
                minPurchaseAmount = 25.0,
                maxDiscountAmount = null,
            ),
            usageLimit = 1000,
            usedCount = 234,
            active = true,
            validFrom = LocalDateTime.now().minusDays(7),
            validUntil = LocalDateTime.now().plusDays(23)
        ),

        // Seasonal Vouchers
        Voucher(
            code = "SPRINGSALE20",
            description = "Spring sale discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 20.0,
            condition = VoucherCondition(
                minPurchaseAmount = 100.0,
                maxDiscountAmount = 50.0,
            ),
            usageLimit = 50,
            usedCount = 32,
            active = true,
            validFrom = LocalDateTime.now().minusDays(15),
            validUntil = LocalDateTime.now().plusDays(45)
        ),
        Voucher(
            code = "SUMMER30",
            description = "Summer blast 30% off",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 30.0,
            condition = VoucherCondition(
                minPurchaseAmount = 120.0,
                maxDiscountAmount = 80.0,
            ),
            usageLimit = 30,
            usedCount = 18,
            active = true,
            validFrom = LocalDateTime.now().minusDays(3),
            validUntil = LocalDateTime.now().plusDays(60)
        ),
        Voucher(
            code = "FALLSPECIAL25",
            description = "Fall special discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 25.0,
            condition = VoucherCondition(
                minPurchaseAmount = 90.0,
                maxDiscountAmount = 60.0,
            ),
            usageLimit = 75,
            usedCount = 43,
            active = true,
            validFrom = LocalDateTime.now().minusDays(12),
            validUntil = LocalDateTime.now().plusDays(30)
        ),
        Voucher(
            code = "WINTERWARM40",
            description = "Winter warm-up sale",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 40.0,
            condition = VoucherCondition(
                minPurchaseAmount = 200.0,
                maxDiscountAmount = 120.0,
            ),
            usageLimit = 25,
            usedCount = 7,
            active = true,
            validFrom = LocalDateTime.now().minusDays(1),
            validUntil = LocalDateTime.now().plusDays(90)
        ),

        // Holiday Special Vouchers
        Voucher(
            code = "BLACKFRIDAY50",
            description = "Black Friday mega discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 50.0,
            condition = VoucherCondition(
                minPurchaseAmount = 150.0,
                maxDiscountAmount = 100.0,
            ),
            usageLimit = 20,
            usedCount = 19,
            active = true,
            validFrom = LocalDateTime.now().minusDays(20),
            validUntil = LocalDateTime.now().plusDays(5)
        ),
        Voucher(
            code = "CYBERMONDAY35",
            description = "Cyber Monday tech deals",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 35.0,
            condition = VoucherCondition(
                minPurchaseAmount = 80.0,
                maxDiscountAmount = 75.0,
                requiredCategoryId = 1L // Electronics category
            ),
            usageLimit = 40,
            usedCount = 28,
            active = true,
            validFrom = LocalDateTime.now().minusDays(18),
            validUntil = LocalDateTime.now().plusDays(7)
        ),
        Voucher(
            code = "NEWYEAR60",
            description = "New Year celebration discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 60.0,
            condition = VoucherCondition(
                minPurchaseAmount = 250.0,
                maxDiscountAmount = 150.0,
            ),
            usageLimit = 15,
            usedCount = 8,
            active = true,
            validFrom = LocalDateTime.now().minusDays(5),
            validUntil = LocalDateTime.now().plusDays(10)
        ),
        Voucher(
            code = "VALENTINE20",
            description = "Valentine's Day special",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 20.0,
            condition = VoucherCondition(
                minPurchaseAmount = 60.0,
                maxDiscountAmount = 40.0,
            ),
            usageLimit = 100,
            usedCount = 67,
            active = true,
            validFrom = LocalDateTime.now().minusDays(8),
            validUntil = LocalDateTime.now().plusDays(22)
        ),
        Voucher(
            code = "EASTER15",
            description = "Easter celebration discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 15.0,
            condition = VoucherCondition(
                minPurchaseAmount = 45.0,
                maxDiscountAmount = 30.0,
            ),
            usageLimit = 80,
            usedCount = 34,
            active = true,
            validFrom = LocalDateTime.now().minusDays(6),
            validUntil = LocalDateTime.now().plusDays(14)
        ),

        // Amount-based Vouchers
        Voucher(
            code = "SAVE10DOLLARS",
            description = "Save $10 on orders over $50",
            discountType = DiscountType.AMOUNT,
            discountValue = 10.0,
            condition = VoucherCondition(
                minPurchaseAmount = 50.0,
                maxDiscountAmount = null,
            ),
            usageLimit = 200,
            usedCount = 89,
            active = true,
            validFrom = LocalDateTime.now().minusDays(10),
            validUntil = LocalDateTime.now().plusDays(20)
        ),
        Voucher(
            code = "BIGSPENDER25",
            description = "$25 off orders over $150",
            discountType = DiscountType.AMOUNT,
            discountValue = 25.0,
            condition = VoucherCondition(
                minPurchaseAmount = 150.0,
                maxDiscountAmount = null,
            ),
            usageLimit = 50,
            usedCount = 23,
            active = true,
            validFrom = LocalDateTime.now().minusDays(7),
            validUntil = LocalDateTime.now().plusDays(28)
        ),
        Voucher(
            code = "MEGA50OFF",
            description = "$50 off on premium orders",
            discountType = DiscountType.AMOUNT,
            discountValue = 50.0,
            condition = VoucherCondition(
                minPurchaseAmount = 300.0,
                maxDiscountAmount = null,
            ),
            usageLimit = 20,
            usedCount = 5,
            active = true,
            validFrom = LocalDateTime.now().minusDays(3),
            validUntil = LocalDateTime.now().plusDays(35)
        ),

        // Free Shipping Vouchers
        Voucher(
            code = "FREESHIP",
            description = "Free shipping on orders over $75",
            discountType = DiscountType.FREE_SHIPPING,
            discountValue = 0.0,
            condition = VoucherCondition(
                minPurchaseAmount = 75.0,
                maxDiscountAmount = null,
            ),
            usageLimit = 200,
            usedCount = 145,
            active = true,
            validFrom = LocalDateTime.now().minusDays(15),
            validUntil = LocalDateTime.now().plusDays(30)
        ),
        Voucher(
            code = "SHIPFREE35",
            description = "Free shipping on any order over $35",
            discountType = DiscountType.FREE_SHIPPING,
            discountValue = 0.0,
            condition = VoucherCondition(
                minPurchaseAmount = 35.0,
                maxDiscountAmount = null,
            ),
            usageLimit = 500,
            usedCount = 287,
            active = true,
            validFrom = LocalDateTime.now().minusDays(20),
            validUntil = LocalDateTime.now().plusDays(40)
        ),
        Voucher(
            code = "EXPRESSSHIP",
            description = "Free express shipping",
            discountType = DiscountType.FREE_SHIPPING,
            discountValue = 0.0,
            condition = VoucherCondition(
                minPurchaseAmount = 100.0,
                maxDiscountAmount = null,
            ),
            usageLimit = 100,
            usedCount = 42,
            active = true,
            validFrom = LocalDateTime.now().minusDays(12),
            validUntil = LocalDateTime.now().plusDays(25)
        ),

        // Buy X Get Y Vouchers
        Voucher(
            code = "BOGO",
            description = "Buy one get one free offer",
            discountType = DiscountType.BUY_X_GET_Y,
            discountValue = 0.0,
            condition = VoucherCondition(
                buyQuantityX = 1,
                getQuantityY = 1,
                minPurchaseAmount = 20.0
            ),
            usageLimit = 100,
            usedCount = 67,
            active = true,
            validFrom = LocalDateTime.now().minusDays(10),
            validUntil = LocalDateTime.now().plusDays(20)
        ),
        Voucher(
            code = "BUY2GET1",
            description = "Buy 2 get 1 free special",
            discountType = DiscountType.BUY_X_GET_Y,
            discountValue = 0.0,
            condition = VoucherCondition(
                buyQuantityX = 2,
                getQuantityY = 1,
                minPurchaseAmount = 40.0
            ),
            usageLimit = 75,
            usedCount = 34,
            active = true,
            validFrom = LocalDateTime.now().minusDays(8),
            validUntil = LocalDateTime.now().plusDays(22)
        ),
        Voucher(
            code = "BUY3GET2",
            description = "Buy 3 get 2 free mega deal",
            discountType = DiscountType.BUY_X_GET_Y,
            discountValue = 0.0,
            condition = VoucherCondition(
                buyQuantityX = 3,
                getQuantityY = 2,
                minPurchaseAmount = 80.0
            ),
            usageLimit = 50,
            usedCount = 19,
            active = true,
            validFrom = LocalDateTime.now().minusDays(5),
            validUntil = LocalDateTime.now().plusDays(30)
        ),

        // Free Gift Vouchers
        Voucher(
            code = "FREEGIFT50",
            description = "Free gift with purchase over $50",
            discountType = DiscountType.FREE_GIFT,
            discountValue = 0.0,
            condition = VoucherCondition(
                minPurchaseAmount = 50.0,
                giftProductId = 101L
            ),
            usageLimit = 150,
            usedCount = 78,
            active = true,
            validFrom = LocalDateTime.now().minusDays(12),
            validUntil = LocalDateTime.now().plusDays(18)
        ),
        Voucher(
            code = "LUXURYGIFT",
            description = "Luxury gift with premium orders",
            discountType = DiscountType.FREE_GIFT,
            discountValue = 0.0,
            condition = VoucherCondition(
                minPurchaseAmount = 200.0,
                giftProductId = 102L
            ),
            usageLimit = 30,
            usedCount = 12,
            active = true,
            validFrom = LocalDateTime.now().minusDays(7),
            validUntil = LocalDateTime.now().plusDays(28)
        ),
        Voucher(
            code = "SAMPLEGIFT25",
            description = "Sample gift with any order over $25",
            discountType = DiscountType.FREE_GIFT,
            discountValue = 0.0,
            condition = VoucherCondition(
                minPurchaseAmount = 25.0,
                giftProductId = 103L
            ),
            usageLimit = 300,
            usedCount = 189,
            active = true,
            validFrom = LocalDateTime.now().minusDays(15),
            validUntil = LocalDateTime.now().plusDays(35)
        ),

        // Category-Specific Vouchers
        Voucher(
            code = "ELECTRONICS20",
            description = "20% off electronics",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 20.0,
            condition = VoucherCondition(
                minPurchaseAmount = 100.0,
                maxDiscountAmount = 150.0,
                requiredCategoryId = 1L
            ),
            usageLimit = 60,
            usedCount = 38,
            active = true,
            validFrom = LocalDateTime.now().minusDays(9),
            validUntil = LocalDateTime.now().plusDays(21)
        ),
        Voucher(
            code = "FASHION25",
            description = "Fashion items 25% off",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 25.0,
            condition = VoucherCondition(
                minPurchaseAmount = 60.0,
                maxDiscountAmount = 80.0,
                requiredCategoryId = 2L
            ),
            usageLimit = 80,
            usedCount = 52,
            active = true,
            validFrom = LocalDateTime.now().minusDays(11),
            validUntil = LocalDateTime.now().plusDays(19)
        ),
        Voucher(
            code = "HOMEANDGARDEN15",
            description = "Home & Garden 15% discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 15.0,
            condition = VoucherCondition(
                minPurchaseAmount = 40.0,
                maxDiscountAmount = 50.0,
                requiredCategoryId = 3L
            ),
            usageLimit = 100,
            usedCount = 67,
            active = true,
            validFrom = LocalDateTime.now().minusDays(6),
            validUntil = LocalDateTime.now().plusDays(24)
        ),
        Voucher(
            code = "BOOKS10OFF",
            description = "$10 off books and media",
            discountType = DiscountType.AMOUNT,
            discountValue = 10.0,
            condition = VoucherCondition(
                minPurchaseAmount = 30.0,
                requiredCategoryId = 4L
            ),
            usageLimit = 120,
            usedCount = 85,
            active = true,
            validFrom = LocalDateTime.now().minusDays(14),
            validUntil = LocalDateTime.now().plusDays(26)
        ),

        // Product-Specific Vouchers
        Voucher(
            code = "IPHONE20OFF",
            description = "iPhone specific discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 20.0,
            condition = VoucherCondition(
                requiredProductId = 1001L,
                maxDiscountAmount = 200.0
            ),
            usageLimit = 25,
            usedCount = 14,
            active = true,
            validFrom = LocalDateTime.now().minusDays(4),
            validUntil = LocalDateTime.now().plusDays(16)
        ),
        Voucher(
            code = "LAPTOP100OFF",
            description = "$100 off premium laptops",
            discountType = DiscountType.AMOUNT,
            discountValue = 100.0,
            condition = VoucherCondition(
                requiredProductId = 1002L,
                minPurchaseAmount = 500.0
            ),
            usageLimit = 15,
            usedCount = 7,
            active = true,
            validFrom = LocalDateTime.now().minusDays(2),
            validUntil = LocalDateTime.now().plusDays(33)
        ),

        // Limited Time Flash Sales
        Voucher(
            code = "FLASH30MIN",
            description = "30-minute flash sale",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 45.0,
            condition = VoucherCondition(
                minPurchaseAmount = 80.0,
                maxDiscountAmount = 100.0,
            ),
            usageLimit = 10,
            usedCount = 8,
            active = true,
            validFrom = LocalDateTime.now().minusMinutes(15),
            validUntil = LocalDateTime.now().plusMinutes(15)
        ),
        Voucher(
            code = "HOURLY40",
            description = "One-hour special discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 40.0,
            condition = VoucherCondition(
                minPurchaseAmount = 70.0,
                maxDiscountAmount = 80.0,
            ),
            usageLimit = 20,
            usedCount = 16,
            active = true,
            validFrom = LocalDateTime.now().minusHours(1),
            validUntil = LocalDateTime.now().plusMinutes(30)
        ),

        // Loyalty & VIP Vouchers
        Voucher(
            code = "VIP30",
            description = "VIP customer exclusive discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 30.0,
            condition = VoucherCondition(
                minPurchaseAmount = 100.0,
                maxDiscountAmount = 120.0,
            ),
            usageLimit = 50,
            usedCount = 27,
            active = true,
            validFrom = LocalDateTime.now().minusDays(30),
            validUntil = LocalDateTime.now().plusDays(60)
        ),
        Voucher(
            code = "LOYALTY25",
            description = "Loyalty program member discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 25.0,
            condition = VoucherCondition(
                minPurchaseAmount = 75.0,
                maxDiscountAmount = 90.0,
            ),
            usageLimit = 100,
            usedCount = 63,
            active = true,
            validFrom = LocalDateTime.now().minusDays(20),
            validUntil = LocalDateTime.now().plusDays(40)
        ),

        // Expired/Inactive Vouchers (for testing)
        Voucher(
            code = "EXPIRED10",
            description = "Expired voucher example",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 10.0,
            condition = VoucherCondition(
                minPurchaseAmount = 50.0,
                maxDiscountAmount = 25.0,
            ),
            usageLimit = 100,
            usedCount = 45,
            active = false,
            expired = true,
            validFrom = LocalDateTime.now().minusDays(60),
            validUntil = LocalDateTime.now().minusDays(30)
        ),
        Voucher(
            code = "INACTIVE15",
            description = "Inactive voucher for testing",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 15.0,
            condition = VoucherCondition(
                minPurchaseAmount = 60.0,
                maxDiscountAmount = 30.0,
            ),
            usageLimit = 50,
            usedCount = 0,
            active = false,
            expired = false,
            validFrom = LocalDateTime.now().minusDays(5),
            validUntil = LocalDateTime.now().plusDays(25)
        ),
        Voucher(
            code = "MAXEDOUT20",
            description = "Fully used voucher",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 20.0,
            condition = VoucherCondition(
                minPurchaseAmount = 40.0,
                maxDiscountAmount = 35.0,
            ),
            usageLimit = 25,
            usedCount = 25, // Fully used
            active = true,
            expired = false,
            validFrom = LocalDateTime.now().minusDays(10),
            validUntil = LocalDateTime.now().plusDays(15)
        ),

        // International/Currency Specific
        Voucher(
            code = "INTERNATIONAL10",
            description = "International shipping discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 10.0,
            condition = VoucherCondition(
                minPurchaseAmount = 100.0,
                maxDiscountAmount = 50.0,
            ),
            usageLimit = 200,
            usedCount = 87,
            active = true,
            validFrom = LocalDateTime.now().minusDays(25),
            validUntil = LocalDateTime.now().plusDays(65)
        ),

        // Bulk Purchase Incentives
        Voucher(
            code = "BULK30",
            description = "Bulk purchase discount",
            discountType = DiscountType.PERCENTAGE,
            discountValue = 30.0,
            condition = VoucherCondition(
                minPurchaseAmount = 500.0,
                maxDiscountAmount = 200.0,
            ),
            usageLimit = 20,
            usedCount = 8,
            active = true,
            validFrom = LocalDateTime.now().minusDays(15),
            validUntil = LocalDateTime.now().plusDays(45)
        ),
        Voucher(
            code = "WHOLESALE50",
            description = "Wholesale customer discount",
            discountType = DiscountType.AMOUNT,
            discountValue = 50.0,
            condition = VoucherCondition(
                minPurchaseAmount = 1000.0,
            ),
            usageLimit = 10,
            usedCount = 3,
            active = true,
            validFrom = LocalDateTime.now().minusDays(30),
            validUntil = LocalDateTime.now().plusDays(90)
        )
    )

    voucherRepository.saveAll(vouchers)
    println("Successfully initialized ${vouchers.size} comprehensive vouchers!")
}