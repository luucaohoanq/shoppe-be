package com.lcaohoanq.sp.domains.product

import com.lcaohoanq.sp.repositories.CategoryRepository
import com.lcaohoanq.sp.repositories.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DisplayName("Product Service Tests")
class ProductServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var categoryRepository: CategoryRepository

    private lateinit var productService: ProductService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        productService = ProductService(productRepository, categoryRepository)
    }

    @Test
    @DisplayName("Should create product successfully")
    fun `should create product successfully`() {
        // Given
        val request = ProductPort.ProductRequest(
            name = "Test Product",
            description = "Test Description",
            price = 99.99,
            stock = 10,
            shopId = 1L,
            categoryId = 1L,
            imageUrl = "",
            weight = 10.2,
            dimensions = "10x10x10"
        )

        val product = Product(
            id = 1L,
            name = request.name,
            description = request.description,
            price = request.price,
            stock = request.stock,
            shopId = request.shopId
        )

        `when`(categoryRepository.existsById(1L)).thenReturn(true)
        `when`(productRepository.save(any(Product::class.java))).thenReturn(product)

        // When
        val result = productService.createProduct(request)

        // Then
        assertNotNull(result)
        assertEquals(request.name, result.name)
        assertEquals(request.price, result.price)
        verify(categoryRepository).existsById(1L)
        verify(productRepository).save(any(Product::class.java))
    }

    @Test
    @DisplayName("Should throw exception when category not found during product creation")
    fun `should throw exception when category not found during product creation`() {
        // Given
        val request = ProductPort.ProductRequest(
            name = "Test Product",
            description = "Test Description",
            price = 99.99,
            stock = 10,
            shopId = 1L,
            categoryId = 999L,
            imageUrl = "",
            weight = 10.2,
            dimensions = "10x10x10"
        )

        `when`(categoryRepository.existsById(999L)).thenReturn(false)

        // When & Then
        assertThrows<Exception> {
            productService.createProduct(request)
        }
        verify(categoryRepository).existsById(999L)
        verify(productRepository, never()).save(any(Product::class.java))
    }

    @Test
    @DisplayName("Should get product by ID successfully")
    fun `should get product by ID successfully`() {
        // Given
        val productId = 1L
        val product = Product(
            id = productId,
            name = "Test Product",
            description = "Test Description",
            price = 99.99,
            stock = 10,
            shopId = 1L
        )

        `when`(productRepository.findById(productId)).thenReturn(Optional.of(product))

        // When
        val result = productService.getProductById(productId)

        // Then
        assertNotNull(result)
        assertEquals(productId, result.id)
        assertEquals(product.name, result.name)
        verify(productRepository).findById(productId)
    }

    @Test
    @DisplayName("Should throw exception when product not found by ID")
    fun `should throw exception when product not found by ID`() {
        // Given
        val productId = 999L
        `when`(productRepository.findById(productId)).thenReturn(Optional.empty())

        // When & Then
        assertThrows<Exception> {
            productService.getProductById(productId)
        }
        verify(productRepository).findById(productId)
    }

    @Test
    @DisplayName("Should update product stock successfully")
    fun `should update product stock successfully`() {
        // Given
        val productId = 1L
        val product = Product(
            id = productId,
            name = "Test Product",
            description = "Test Description",
            price = 99.99,
            stock = 10,
            shopId = 1L
        )
        
        val stockRequest = ProductPort.ProductStockUpdateRequest(
            quantity = 5,
            operation = "SUBTRACT"
        )

        `when`(productRepository.findById(productId)).thenReturn(Optional.of(product))
        `when`(productRepository.save(any(Product::class.java))).thenReturn(product)

        // When
        val result = productService.updateStock(productId, stockRequest)

        // Then
        assertNotNull(result)
        assertEquals(5, result.stock) // 10 - 5 = 5
        verify(productRepository).findById(productId)
        verify(productRepository).save(any(Product::class.java))
    }

    @Test
    @DisplayName("Should check product availability correctly")
    fun `should check product availability correctly`() {
        // Given
        val productId = 1L
        `when`(productRepository.isProductAvailable(productId)).thenReturn(true)

        // When
        val result = productService.isProductAvailable(productId)

        // Then
        assertTrue(result)
        verify(productRepository).isProductAvailable(productId)
    }

//    @Test
//    @DisplayName("Should search products with filters")
//    fun `should search products with filters`() {
//        // Given
//        val searchRequest = ProductPort.ProductSearchRequest(
//            name = "Test",
//            minPrice = 50.0,
//            maxPrice = 200.0,
//            status = Product.ProductStatus.ACTIVE
//        )
//
//        val pageable = PageRequest.of(0, 10)
//        val products = listOf(
//            Product(
//                id = 1L,
//                name = "Test Product 1",
//                description = "Description 1",
//                price = 99.99,
//                stock = 10,
//                shopId = 1L
//            )
//        )
//        val page: Page<Product> = PageImpl(products, pageable, 1)
//
//        `when`(productRepository.findAll(any(), eq(pageable))).thenReturn(page)
//
//        // When
//        val result = productService.searchProducts(searchRequest, pageable)
//
//        // Then
//        assertNotNull(result)
//        assertEquals(1, result.data?.size ?: 0)
//        assertEquals("Test Product 1", result.data?.find { it.name == "Test Product 1" }?.name)
//        verify(productRepository).findAll(any(), eq(pageable))
//    }

    @Test
    @DisplayName("Should get available products")
    fun `should get available products`() {
        // Given
        val pageable = PageRequest.of(0, 10)
        val products = listOf(
            Product(
                id = 1L,
                name = "Available Product",
                description = "Description",
                price = 99.99,
                stock = 10,
                shopId = 1L,
                status = Product.ProductStatus.ACTIVE
            )
        )
        val page: Page<Product> = PageImpl(products, pageable, 1)

        `when`(productRepository.findAvailableProducts(pageable)).thenReturn(page)

        // When
        val result = productService.getAvailableProducts(pageable)

        // Then
        assertNotNull(result)
        assertEquals(1, result.data?.size ?: 0)
        assertTrue(result.data?.any { it.status == Product.ProductStatus.ACTIVE } ?: false)
        verify(productRepository).findAvailableProducts(pageable)
    }

    @Test
    @DisplayName("Should get product summary statistics")
    fun `should get product summary statistics`() {
        // Given
        `when`(productRepository.count()).thenReturn(100L)
        `when`(productRepository.countByStatus(Product.ProductStatus.ACTIVE)).thenReturn(80L)
        `when`(productRepository.countOutOfStock()).thenReturn(5L)
        `when`(productRepository.getAveragePrice()).thenReturn(75.50)
        `when`(productRepository.getTotalInventoryValue()).thenReturn(15000.0)

        // When
        val result = productService.getProductSummary()

        // Then
        assertNotNull(result)
        assertEquals(100L, result.totalProducts)
        assertEquals(80L, result.activeProducts)
        assertEquals(5L, result.outOfStockProducts)
        assertEquals(75.50, result.averagePrice)
        assertEquals(15000.0, result.totalValue)
    }

    @Test
    @DisplayName("Product should update availability when stock changes")
    fun `product should update availability when stock changes`() {
        // Given
        val product = Product(
            id = 1L,
            name = "Test Product",
            description = "Test Description",
            price = 99.99,
            stock = 1,
            shopId = 1L,
            status = Product.ProductStatus.ACTIVE
        )

        // When - Product is available initially
        assertTrue(product.isAvailable())

        // When - Stock becomes 0
        product.updateStock(1)

        // Then - Product should be out of stock
        assertEquals(0, product.stock)
        assertEquals(Product.ProductStatus.OUT_OF_STOCK, product.status)
        assertFalse(product.isAvailable())

        // When - Add stock back
        product.addStock(5)

        // Then - Product should be available again
        assertEquals(5, product.stock)
        assertEquals(Product.ProductStatus.ACTIVE, product.status)
        assertTrue(product.isAvailable())
    }

    @Test
    @DisplayName("Product rating should update correctly")
    fun `product rating should update correctly`() {
        // Given
        val product = Product(
            id = 1L,
            name = "Test Product",
            description = "Test Description",
            price = 99.99,
            stock = 10,
            shopId = 1L,
            rating = 4.0,
            reviewCount = 2
        )

        // When - Add new rating
        product.updateRating(5.0)

        // Then - Rating and review count should be updated
        assertEquals(3, product.reviewCount)
        assertEquals(4.33, product.rating, 0.01) // (4.0*2 + 5.0) / 3 = 4.33
    }
}
