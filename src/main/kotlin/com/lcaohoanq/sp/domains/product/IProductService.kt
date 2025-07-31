package com.lcaohoanq.sp.domains.product

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.utils.Sortable
import org.springframework.data.domain.Pageable

interface IProductService {
    
    // Basic CRUD operations
    fun createProduct(request: ProductPort.ProductRequest): ProductPort.ProductResponse
    fun getProductById(id: Long): ProductPort.ProductResponse
    fun getAllProducts(): List<ProductPort.ProductResponse>
    fun getAllProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    fun getAllProducts(pageable: Pageable, queryCriteria: QueryCriteria<Sortable.ProductSortField>): PageResponse<ProductPort.ProductResponse>
    fun updateProduct(id: Long, request: ProductPort.ProductUpdateRequest): ProductPort.ProductResponse
    fun deleteProduct(id: Long)
    
    // Stock management
    fun updateStock(id: Long, request: ProductPort.ProductStockUpdateRequest): ProductPort.ProductResponse
    fun addStock(id: Long, quantity: Int): ProductPort.ProductResponse
    fun subtractStock(id: Long, quantity: Int): ProductPort.ProductResponse
    fun checkStock(id: Long): Int
    
    // Search and filtering
    fun searchProducts(request: ProductPort.ProductSearchRequest, pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    fun searchProductsByName(name: String, pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    fun getProductsByCategory(categoryId: Long, pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    fun getProductsByShop(shopId: Long, pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    fun getProductsByPriceRange(minPrice: Double, maxPrice: Double, pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    
    // Status management
    fun activateProduct(id: Long): ProductPort.ProductResponse
    fun deactivateProduct(id: Long): ProductPort.ProductResponse
    fun setProductStatus(id: Long, status: Product.ProductStatus): ProductPort.ProductResponse
    
    // Special queries
    fun getAvailableProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    fun getTopSellingProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    fun getTopRatedProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    fun getRecentProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse>
    fun getLowStockProducts(threshold: Int): List<ProductPort.ProductResponse>
    
    // Utility methods
    fun isProductAvailable(id: Long): Boolean
    fun updateRating(id: Long, rating: Double): ProductPort.ProductResponse
    fun incrementSoldCount(id: Long, quantity: Int): ProductPort.ProductResponse
    
    // Statistics
    fun getProductSummary(): ProductPort.ProductSummaryResponse
    fun getProductCountByCategory(categoryId: Long): Long
    fun getProductCountByShop(shopId: Long): Long
}
