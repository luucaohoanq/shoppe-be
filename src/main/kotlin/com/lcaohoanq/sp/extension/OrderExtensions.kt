package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.order.Order
import com.lcaohoanq.sp.domains.order.OrderItem
import com.lcaohoanq.sp.domains.order.OrderPort

/**
 * Extension function to convert Order entity to OrderResponse
 */
fun Order.toOrderResponse(): OrderPort.OrderResponse {
    return OrderPort.OrderResponse(
        id = this.id ?: 0,
        orderNumber = this.orderNumber,
        userId = this.userId,
        status = this.status,
        addressId = this.addressId,
        shippingMethodId = this.shippingMethodId,
        shippingCost = this.shippingFee,
        estimatedDelivery = this.estimatedDelivery,
        couponId = this.couponId,
        discountAmount = this.discountAmount,
        subtotal = this.calculateSubtotal(),
        totalAmount = this.calculateTotal(),
        notes = this.notes,
        items = this.items.map { it.toOrderItemResponse() },
        createdAt = this.createdAt,
        updatedAt = this.lastModifiedOn,
        paymentStatus = this.paymentStatus,
        shippingStatus = this.shippingStatus,
        shippingFee = this.shippingFee,
        taxAmount = this.taxAmount,
        deliveredAt = this.deliveredAt,
        address = TODO(),
        shippingMethod = TODO(),
        canBeCancelled = TODO(),
        canBeModified = TODO(),
        isCompleted = TODO(),
    )
}

/**
 * Extension function to convert Order entity to OrderSummaryResponse
 * (a simplified version with less details)
 */
fun Order.toOrderSummaryResponse(): OrderPort.OrderSummaryResponse {
    return OrderPort.OrderSummaryResponse(
        id = this.id ?: 0,
        orderNumber = this.orderNumber,
        status = this.status,
        totalAmount = this.calculateTotal(),
        totalItems = this.items.size,
        createdAt = this.createdAt
    )
}

/**
 * Extension function to convert OrderItem to OrderItemResponse
 */
private fun OrderItem.toOrderItemResponse(): OrderPort.OrderItemResponse {
    return OrderPort.OrderItemResponse(
        id = this.id ?: 0,
        productId = this.productId,
        productName = this.productName,
        productImage = this.productImage,
        quantity = this.quantity,
        unitPrice = this.unitPrice,
        totalPrice = this.calculateTotalPrice()
    )
}