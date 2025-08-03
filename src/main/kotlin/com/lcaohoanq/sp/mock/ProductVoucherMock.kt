package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.domains.discount.ProductVoucher
import com.lcaohoanq.sp.domains.discount.Voucher
import com.lcaohoanq.sp.domains.product.Product
import com.lcaohoanq.sp.repositories.ProductVoucherRepository

fun initProductVoucherRelationships(
    productVoucherRepository: ProductVoucherRepository,
    products: List<Product>,
    vouchers: List<Voucher>
) {
    if (products.isEmpty() || vouchers.isEmpty()) {
        println("No products or vouchers available for creating relationships")
        return
    }

    val productVouchers = mutableListOf<ProductVoucher>()

    // Create relationships between products and vouchers
    products.forEachIndexed { productIndex, product ->
        // Each product gets 2-4 random vouchers
        val voucherCount = (2..4).random()
        val selectedVouchers = vouchers.shuffled().take(voucherCount)
        
        selectedVouchers.forEach { voucher ->
            productVouchers.add(
                ProductVoucher(
                    product = product,
                    voucher = voucher
                )
            )
        }
    }

    // Create some category-specific voucher relationships
    // Electronics products get tech-related vouchers
    val electronicsProducts = products.filter { 
        it.sku?.startsWith("ELEC") == true 
    }
    val techVouchers = vouchers.filter { 
        it.code.contains("CYBER") || it.code.contains("TECH") || it.code.contains("BLACKFRIDAY")
    }
    
    electronicsProducts.forEach { product ->
        techVouchers.forEach { voucher ->
            // Avoid duplicates
            val exists = productVouchers.any { 
                it.product?.id == product.id && it.voucher?.id == voucher.id 
            }
            if (!exists) {
                productVouchers.add(
                    ProductVoucher(
                        product = product,
                        voucher = voucher
                    )
                )
            }
        }
    }

    // Clothing products get fashion-related vouchers
    val clothingProducts = products.filter { 
        it.sku?.startsWith("CLOTH") == true 
    }
    val fashionVouchers = vouchers.filter { 
        it.code.contains("VALENTINE") || it.code.contains("SPRING") || it.code.contains("FASHION")
    }
    
    clothingProducts.forEach { product ->
        fashionVouchers.forEach { voucher ->
            val exists = productVouchers.any { 
                it.product?.id == product.id && it.voucher?.id == voucher.id 
            }
            if (!exists) {
                productVouchers.add(
                    ProductVoucher(
                        product = product,
                        voucher = voucher
                    )
                )
            }
        }
    }

    // Home products get home-related vouchers
    val homeProducts = products.filter { 
        it.sku?.startsWith("HOME") == true 
    }
    val homeVouchers = vouchers.filter { 
        it.code.contains("HOME") || it.code.contains("KITCHEN") || it.code.contains("WINTER")
    }
    
    homeProducts.forEach { product ->
        homeVouchers.forEach { voucher ->
            val exists = productVouchers.any { 
                it.product?.id == product.id && it.voucher?.id == voucher.id 
            }
            if (!exists) {
                productVouchers.add(
                    ProductVoucher(
                        product = product,
                        voucher = voucher
                    )
                )
            }
        }
    }

    // High-value products get premium vouchers
    val premiumProducts = products.filter { it.price > 100.0 }
    val premiumVouchers = vouchers.filter { 
        it.code.contains("VIP") || it.code.contains("PREMIUM") || it.code.contains("MEGA") 
    }
    
    premiumProducts.forEach { product ->
        premiumVouchers.forEach { voucher ->
            val exists = productVouchers.any { 
                it.product?.id == product.id && it.voucher?.id == voucher.id 
            }
            if (!exists) {
                productVouchers.add(
                    ProductVoucher(
                        product = product,
                        voucher = voucher
                    )
                )
            }
        }
    }

    productVoucherRepository.saveAll(productVouchers)
    println("Successfully created ${productVouchers.size} product-voucher relationships!")
}
