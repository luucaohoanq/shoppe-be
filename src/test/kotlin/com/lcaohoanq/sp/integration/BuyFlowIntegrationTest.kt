package com.lcaohoanq.sp.integration

import com.lcaohoanq.sp.domains.cart.CartPort
import com.lcaohoanq.sp.domains.cart.ICartService
import com.lcaohoanq.sp.domains.order.IOrderService
import com.lcaohoanq.sp.domains.order.Order
import com.lcaohoanq.sp.domains.order.OrderPort
import com.lcaohoanq.sp.domains.payment.IPaymentService
import com.lcaohoanq.sp.domains.product.IProductService
import com.lcaohoanq.sp.domains.product.ProductPort
import com.lcaohoanq.sp.entities.Payment
import com.lcaohoanq.sp.entities.Product
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class BuyFlowIntegrationTest {

    @Autowired
    private lateinit var productService: IProductService
    
    @Autowired
    private lateinit var cartService: ICartService
    
    @Autowired
    private lateinit var orderService: IOrderService
    
    @Autowired
    private lateinit var paymentService: IPaymentService

    @Test
    fun `complete buy flow - from product to order completion`() {
        val userId = 1L
        
        // Step 1: Create a test product
        val createProductRequest = ProductPort.CreateProductRequest(
            name = "Test Product for Buy Flow",
            description = "A test product for integration testing",
            price = 99.99,
            stock = 100,
            categoryId = 1,
            shopId = 1,
            imageUrl = "https://example.com/test-product.jpg"
        )
        
        val createdProduct = productService.createProduct(createProductRequest)
        assertNotNull(createdProduct)
        assertEquals("Test Product for Buy Flow", createdProduct.name)
        assertEquals(99.99, createdProduct.price)
        assertEquals(100, createdProduct.stock)
        
        // Step 2: Add product to cart
        val addToCartRequest = CartPort.AddToCartRequest(
            productId = createdProduct.id,
            quantity = 2
        )
        
        val cartItem = cartService.addToCart(userId, addToCartRequest)
        assertNotNull(cartItem)
        assertEquals(createdProduct.id, cartItem.productId)
        assertEquals(2, cartItem.quantity)
        assertEquals(199.98, cartItem.totalPrice) // 99.99 * 2
        
        // Step 3: Get and validate cart
        val cart = cartService.getCart(userId)
        assertNotNull(cart)
        assertEquals(1, cart.items.size)
        assertEquals(2, cart.totalItems)
        assertEquals(199.98, cart.subtotal)
        
        val validation = cartService.validateCartItems(userId)
        assertTrue(validation.isValid)
        assertEquals(0, validation.unavailableItems.size)
        assertEquals(0, validation.insufficientStockItems.size)
        
        // Step 4: Checkout from cart
        val checkoutRequest = OrderPort.CheckoutRequest(
            addressId = 1L, // Assuming address exists
            shippingMethodId = 1L, // Assuming shipping method exists
            couponId = null,
            paymentMethod = Payment.PaymentMethod.CREDIT_CARD,
            notes = "Integration test order",
            useCartItems = true,
            selectedCartItems = null
        )
        
        val checkoutResponse = orderService.checkoutFromCart(userId, checkoutRequest)
        assertNotNull(checkoutResponse)
        assertNotNull(checkoutResponse.order)
        assertNotNull(checkoutResponse.paymentUrl)
        assertNotNull(checkoutResponse.paymentInstructions)
        
        val order = checkoutResponse.order
        assertEquals(userId, order.userId)
        assertEquals(Order.OrderStatus.PENDING, order.status)
        assertEquals(1, order.items.size)
        assertEquals(createdProduct.id, order.items[0].productId)
        assertEquals(2, order.items[0].quantity)
        assertTrue(order.totalAmount > 199.98) // Should include shipping
        
        // Step 5: Verify cart is cleared
        val cartAfterCheckout = cartService.getCart(userId)
        assertTrue(cartAfterCheckout?.isEmpty ?: true)
        
        // Step 6: Verify payment was created
        val payment = paymentService.getPaymentByOrderId(order.id)
        assertNotNull(payment)
        assertEquals(order.id, payment.orderId)
        assertEquals(order.totalAmount, payment.amount)
        assertEquals(Payment.PaymentMethod.CREDIT_CARD, payment.paymentMethod)
        assertEquals(Payment.PaymentStatus.PENDING, payment.status)
        
        // Step 7: Process payment
        val processedPayment = paymentService.processPayment(payment.id)
        assertNotNull(processedPayment)
        assertTrue(
            processedPayment.status == Payment.PaymentStatus.COMPLETED ||
            processedPayment.status == Payment.PaymentStatus.FAILED
        )
        
        // Step 8: Verify stock was updated
        val updatedProduct = productService.getProductById(createdProduct.id)
        assertEquals(98, updatedProduct.stock) // 100 - 2 = 98
        
        // Step 9: Test order status updates (admin operations)
        if (order.status == Order.OrderStatus.PENDING) {
            val confirmedOrder = orderService.confirmOrder(order.id)
            assertEquals(Order.OrderStatus.CONFIRMED, confirmedOrder.status)
            
            val shippedOrder = orderService.shipOrder(order.id, "TRACK123")
            assertEquals(Order.OrderStatus.SHIPPED, shippedOrder.status)
            
            val deliveredOrder = orderService.deliverOrder(order.id)
            assertEquals(Order.OrderStatus.DELIVERED, deliveredOrder.status)
        }
        
        // Step 10: Verify order can be retrieved by user
        val retrievedOrder = orderService.getOrderById(order.id, userId)
        assertEquals(order.id, retrievedOrder.id)
        assertEquals(order.orderNumber, retrievedOrder.orderNumber)
        
        // Step 11: Test order search
        val searchRequest = OrderPort.OrderSearchRequest(
            orderNumber = order.orderNumber,
            status = null,
            startDate = null,
            endDate = null,
            minAmount = null,
            maxAmount = null
        )
        
        val searchResults = orderService.searchUserOrders(
            userId, 
            searchRequest, 
            org.springframework.data.domain.PageRequest.of(0, 10)
        )
        
        assertTrue(searchResults.data.isNotEmpty())
        assertTrue(searchResults.data.any { it.orderNumber == order.orderNumber })
    }
    
    @Test
    fun `buy flow with insufficient stock should fail`() {
        val userId = 1L
        
        // Create a product with limited stock
        val createProductRequest = ProductPort.CreateProductRequest(
            name = "Limited Stock Product",
            description = "A product with limited stock",
            price = 50.0,
            stock = 1, // Only 1 item in stock
            categoryId = 1,
            shopId = 1,
            imageUrl = "https://example.com/limited-product.jpg"
        )
        
        val product = productService.createProduct(createProductRequest)
        
        // Try to add more items than available
        val addToCartRequest = CartPort.AddToCartRequest(
            productId = product.id,
            quantity = 5 // Requesting more than available
        )
        
        try {
            cartService.addToCart(userId, addToCartRequest)
            // If we reach here, the test should fail
            assertTrue(false, "Should have thrown an exception for insufficient stock")
        } catch (e: Exception) {
            // Expected to fail due to insufficient stock
            assertTrue(e.message?.contains("Insufficient stock") ?: false)
        }
    }
    
    @Test
    fun `order cancellation should restore stock`() {
        val userId = 1L
        
        // Create a product
        val createProductRequest = ProductPort.CreateProductRequest(
            name = "Cancellable Product",
            description = "A product for testing cancellation",
            price = 75.0,
            stock = 10,
            categoryId = 1,
            shopId = 1,
            imageUrl = "https://example.com/cancellable-product.jpg"
        )
        
        val product = productService.createProduct(createProductRequest)
        val originalStock = product.stock
        
        // Create order directly
        val orderRequest = OrderPort.CreateOrderRequest(
            addressId = 1L,
            shippingMethodId = 1L,
            couponId = null,
            notes = "Test order for cancellation",
            items = listOf(
                OrderPort.OrderItemRequest(
                    productId = product.id,
                    quantity = 3
                )
            )
        )
        
        val order = orderService.createOrder(userId, orderRequest)
        
        // Verify stock was reduced
        val productAfterOrder = productService.getProductById(product.id)
        assertEquals(originalStock - 3, productAfterOrder.stock)
        
        // Cancel the order
        val cancelledOrder = orderService.cancelOrder(order.id, userId, "Changed my mind")
        assertEquals(Order.OrderStatus.CANCELLED, cancelledOrder.status)
        
        // Verify stock was restored
        val productAfterCancellation = productService.getProductById(product.id)
        assertEquals(originalStock, productAfterCancellation.stock)
    }
}
