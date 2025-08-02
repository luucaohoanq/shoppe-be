package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.entities.ShippingMethod
import com.lcaohoanq.sp.repositories.ShippingMethodRepository

fun initShippingMethods(shippingMethodRepository: ShippingMethodRepository) {
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