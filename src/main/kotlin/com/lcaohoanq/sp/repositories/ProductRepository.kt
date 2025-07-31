package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.product.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    
    // Find by basic fields
    fun findByName(name: String): List<Product>
    fun findByNameContainingIgnoreCase(name: String): List<Product>
    fun findByCategoryId(categoryId: Long): List<Product>
    fun findByShopId(shopId: Long): List<Product>
    fun findByStatus(status: Product.ProductStatus): List<Product>
    
    // Price range queries
    fun findByPriceBetween(minPrice: Double, maxPrice: Double): List<Product>
    fun findByPriceGreaterThanEqual(price: Double): List<Product>
    fun findByPriceLessThanEqual(price: Double): List<Product>
    
    // Stock queries
    fun findByStockGreaterThan(stock: Int): List<Product>
    fun findByStockLessThanEqual(stock: Int): List<Product>
    fun findByStockEquals(stock: Int): List<Product>
    
    // Combined queries
    fun findByCategoryIdAndStatus(categoryId: Long, status: Product.ProductStatus): List<Product>
    fun findByShopIdAndStatus(shopId: Long, status: Product.ProductStatus): List<Product>
    fun findByCategoryIdAndPriceBetween(categoryId: Long, minPrice: Double, maxPrice: Double): List<Product>
    
    // Pageable queries
    fun findByCategoryId(categoryId: Long, pageable: Pageable): Page<Product>
    fun findByShopId(shopId: Long, pageable: Pageable): Page<Product>
    fun findByStatus(status: Product.ProductStatus, pageable: Pageable): Page<Product>
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Product>
    
    // Custom queries
    @Query("SELECT p FROM Product p WHERE p.stock > 0 AND p.status = 'ACTIVE'")
    fun findAvailableProducts(): List<Product>
    
    @Query("SELECT p FROM Product p WHERE p.stock > 0 AND p.status = 'ACTIVE'")
    fun findAvailableProducts(pageable: Pageable): Page<Product>
    
    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId AND p.stock > 0 AND p.status = 'ACTIVE'")
    fun findAvailableProductsByCategory(@Param("categoryId") categoryId: Long): List<Product>
    
    @Query("SELECT p FROM Product p WHERE p.shopId = :shopId AND p.stock > 0 AND p.status = 'ACTIVE'")
    fun findAvailableProductsByShop(@Param("shopId") shopId: Long): List<Product>
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    fun searchProducts(@Param("keyword") keyword: String): List<Product>
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    fun searchProducts(@Param("keyword") keyword: String, pageable: Pageable): Page<Product>
    
    // Top selling products
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' ORDER BY p.soldCount DESC")
    fun findTopSellingProducts(pageable: Pageable): Page<Product>
    
    // Top rated products
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.reviewCount > 0 ORDER BY p.rating DESC")
    fun findTopRatedProducts(pageable: Pageable): Page<Product>
    
    // Recently added products
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' ORDER BY p.createdAt DESC")
    fun findRecentProducts(pageable: Pageable): Page<Product>
    
    // Statistics queries
    @Query("SELECT COUNT(p) FROM Product p WHERE p.status = :status")
    fun countByStatus(@Param("status") status: Product.ProductStatus): Long
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.stock > 0")
    fun countInStock(): Long
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.stock = 0")
    fun countOutOfStock(): Long
    
    @Query("SELECT AVG(p.price) FROM Product p WHERE p.status = 'ACTIVE'")
    fun getAveragePrice(): Double?
    
    @Query("SELECT SUM(p.price * p.stock) FROM Product p WHERE p.status = 'ACTIVE'")
    fun getTotalInventoryValue(): Double?
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.categoryId = :categoryId")
    fun countByCategory(@Param("categoryId") categoryId: Long): Long
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.shopId = :shopId")
    fun countByShop(@Param("shopId") shopId: Long): Long
    
    // Advanced search
    @Query("""
        SELECT p FROM Product p 
        WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:categoryId IS NULL OR p.categoryId = :categoryId)
        AND (:shopId IS NULL OR p.shopId = :shopId)
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND (:status IS NULL OR p.status = :status)
        AND (:inStock IS NULL OR (:inStock = true AND p.stock > 0) OR (:inStock = false AND p.stock = 0))
    """)
    fun findProductsWithFilters(
        @Param("name") name: String?,
        @Param("categoryId") categoryId: Long?,
        @Param("shopId") shopId: Long?,
        @Param("minPrice") minPrice: Double?,
        @Param("maxPrice") maxPrice: Double?,
        @Param("status") status: Product.ProductStatus?,
        @Param("inStock") inStock: Boolean?,
        pageable: Pageable
    ): Page<Product>
    
    // Check if product exists and is available
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.id = :id AND p.stock > 0 AND p.status = 'ACTIVE'")
    fun isProductAvailable(@Param("id") id: Long): Boolean
    
    // Find products with low stock
    @Query("SELECT p FROM Product p WHERE p.stock <= :threshold AND p.status = 'ACTIVE'")
    fun findLowStockProducts(@Param("threshold") threshold: Int): List<Product>
}
