package com.lcaohoanq.sp.domains.product

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.exceptions.base.DataNotFoundException
import com.lcaohoanq.sp.extension.toProduct
import com.lcaohoanq.sp.extension.toProductResponse
import com.lcaohoanq.sp.extension.updateFromRequest
import com.lcaohoanq.sp.metadata.PaginationMeta
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.repositories.CategoryRepository
import com.lcaohoanq.sp.repositories.ProductRepository
import com.lcaohoanq.sp.utils.SortCriterion
import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.Sortable
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : IProductService {

    private val log = KotlinLogging.logger {}

    override fun createProduct(request: ProductPort.ProductRequest): ProductPort.ProductResponse {
        log.info { "Creating new product with name: ${request.name}" }
        
        // Validate category exists
        if (!categoryRepository.existsById(request.categoryId)) {
            throw DataNotFoundException("Category with ID ${request.categoryId} not found")
        }

        val product = request.toProduct()
        val savedProduct = productRepository.save(product)
        
        log.info { "Product created successfully with ID: ${savedProduct.id}" }
        return savedProduct.toProductResponse()
    }

    @Transactional(readOnly = true)
    override fun getProductById(id: Long): ProductPort.ProductResponse {
        log.info { "Fetching product with ID: $id" }
        
        val product = productRepository.findById(id)
            .orElseThrow { DataNotFoundException("Product with ID $id not found") }
        
        return product.toProductResponse()
    }

    @Transactional(readOnly = true)
    override fun getAllProducts(): List<ProductPort.ProductResponse> {
        log.info { "Fetching all products" }
        return productRepository.findAll().map { it.toProductResponse() }
    }

    override fun getAll(pageable: Pageable): Page<Product> = productRepository.findAll(pageable)

    @Transactional(readOnly = true)
    override fun getAllProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse> {
        log.info { "Fetching paginated products" }
        
        val pageResult = productRepository.findAll(pageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Get products successfully",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getAllProducts(
        pageable: Pageable,
        queryCriteria: QueryCriteria<Sortable.ProductSortField>
    ): PageResponse<ProductPort.ProductResponse> {
        log.info { "Fetching products with search criteria: ${queryCriteria.search}" }
        
        val searchSpecification = ProductSpecification(queryCriteria.search)
        val sortField = queryCriteria.sortBy.field
        val sortOrder = if (queryCriteria.sortOrder == SortOrder.ASC) Sort.Direction.ASC else Sort.Direction.DESC
        val sortedPageable = PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            Sort.by(sortOrder, sortField)
        )

        val pageResult = productRepository.findAll(searchSpecification, sortedPageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Get products successfully with query",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size,
                search = queryCriteria.search,
                sort = SortCriterion(
                    sortBy = queryCriteria.sortBy,
                    order = queryCriteria.sortOrder
                )
            )
        )
    }

    override fun updateProduct(id: Long, request: ProductPort.ProductUpdateRequest): ProductPort.ProductResponse {
        log.info { "Updating product with ID: $id" }
        
        val product = productRepository.findById(id)
            .orElseThrow { DataNotFoundException("Product with ID $id not found") }

        // Validate category exists if being updated
        request.categoryId?.let { categoryId ->
            if (!categoryRepository.existsById(categoryId)) {
                throw DataNotFoundException("Category with ID $categoryId not found")
            }
        }

        product.updateFromRequest(request)
        val updatedProduct = productRepository.save(product)
        
        log.info { "Product updated successfully with ID: ${updatedProduct.id}" }
        return updatedProduct.toProductResponse()
    }

    override fun deleteProduct(id: Long) {
        log.info { "Deleting product with ID: $id" }
        
        if (!productRepository.existsById(id)) {
            throw DataNotFoundException("Product with ID $id not found")
        }
        
        productRepository.deleteById(id)
        log.info { "Product deleted successfully with ID: $id" }
    }

    override fun updateStock(id: Long, request: ProductPort.ProductStockUpdateRequest): ProductPort.ProductResponse {
        log.info { "Updating stock for product ID: $id, operation: ${request.operation}, quantity: ${request.quantity}" }
        
        val product = productRepository.findById(id)
            .orElseThrow { DataNotFoundException("Product with ID $id not found") }

        when (request.operation.uppercase()) {
            "ADD" -> product.addStock(request.quantity)
            "SUBTRACT" -> product.updateStock(request.quantity)
            else -> throw IllegalArgumentException("Invalid operation: ${request.operation}")
        }

        val updatedProduct = productRepository.save(product)
        log.info { "Stock updated successfully for product ID: $id, new stock: ${updatedProduct.stock}" }
        return updatedProduct.toProductResponse()
    }

    override fun addStock(id: Long, quantity: Int): ProductPort.ProductResponse {
        return updateStock(id, ProductPort.ProductStockUpdateRequest(quantity, "ADD"))
    }

    override fun subtractStock(id: Long, quantity: Int): ProductPort.ProductResponse {
        return updateStock(id, ProductPort.ProductStockUpdateRequest(quantity, "SUBTRACT"))
    }

    @Transactional(readOnly = true)
    override fun checkStock(id: Long): Int {
        val product = productRepository.findById(id)
            .orElseThrow { DataNotFoundException("Product with ID $id not found") }
        return product.stock
    }

    @Transactional(readOnly = true)
    override fun searchProducts(
        request: ProductPort.ProductSearchRequest,
        pageable: Pageable
    ): PageResponse<ProductPort.ProductResponse> {
        log.info { "Searching products with filters" }
        
        val specification = ProductSpecification.withFilters(
            request.name,
            request.categoryId,
            request.shopId,
            request.minPrice,
            request.maxPrice,
            request.status,
            request.inStock
        )

        val sortField = request.sortBy.field
        val sortOrder = if (request.sortOrder.uppercase() == "ASC") Sort.Direction.ASC else Sort.Direction.DESC
        val sortedPageable = PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            Sort.by(sortOrder, sortField)
        )

        val pageResult = productRepository.findAll(specification, sortedPageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Search products successfully",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun searchProductsByName(name: String, pageable: Pageable): PageResponse<ProductPort.ProductResponse> {
        log.info { "Searching products by name: $name" }
        
        val pageResult = productRepository.findByNameContainingIgnoreCase(name, pageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Search products by name successfully",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getProductsByCategory(categoryId: Long, pageable: Pageable): PageResponse<ProductPort.ProductResponse> {
        log.info { "Fetching products by category ID: $categoryId" }
        
        val pageResult = productRepository.findByCategoryId(categoryId, pageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Get products by category successfully",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getProductsByShop(shopId: Long, pageable: Pageable): PageResponse<ProductPort.ProductResponse> {
        log.info { "Fetching products by shop ID: $shopId" }
        
        val pageResult = productRepository.findByShopId(shopId, pageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Get products by shop successfully",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getProductsByPriceRange(
        minPrice: Double,
        maxPrice: Double,
        pageable: Pageable
    ): PageResponse<ProductPort.ProductResponse> {
        log.info { "Fetching products by price range: $minPrice - $maxPrice" }
        
        val products = productRepository.findByPriceBetween(minPrice, maxPrice)
        val productResponses = products.map { it.toProductResponse() }

        // Manual pagination for non-pageable repository methods
        val startIndex = pageable.pageNumber * pageable.pageSize
        val endIndex = minOf(startIndex + pageable.pageSize, productResponses.size)
        val paginatedProducts = if (startIndex < productResponses.size) {
            productResponses.subList(startIndex, endIndex)
        } else {
            emptyList()
        }

        val totalPages = (productResponses.size + pageable.pageSize - 1) / pageable.pageSize

        return PageResponse(
            message = "Get products by price range successfully",
            data = paginatedProducts,
            paginationMeta = PaginationMeta(
                totalPages = totalPages,
                totalItems = productResponses.size.toLong(),
                currentPage = pageable.pageNumber,
                pageSize = pageable.pageSize
            )
        )
    }

    override fun activateProduct(id: Long): ProductPort.ProductResponse {
        return setProductStatus(id, Product.ProductStatus.ACTIVE)
    }

    override fun deactivateProduct(id: Long): ProductPort.ProductResponse {
        return setProductStatus(id, Product.ProductStatus.INACTIVE)
    }

    override fun setProductStatus(id: Long, status: Product.ProductStatus): ProductPort.ProductResponse {
        log.info { "Setting product status for ID: $id to $status" }
        
        val product = productRepository.findById(id)
            .orElseThrow { DataNotFoundException("Product with ID $id not found") }

        product.status = status
        val updatedProduct = productRepository.save(product)
        
        log.info { "Product status updated successfully for ID: $id" }
        return updatedProduct.toProductResponse()
    }

    @Transactional(readOnly = true)
    override fun getAvailableProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse> {
        log.info { "Fetching available products" }
        
        val pageResult = productRepository.findAvailableProducts(pageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Get available products successfully",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getTopSellingProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse> {
        log.info { "Fetching top selling products" }
        
        val pageResult = productRepository.findTopSellingProducts(pageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Get top selling products successfully",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getTopRatedProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse> {
        log.info { "Fetching top rated products" }
        
        val pageResult = productRepository.findTopRatedProducts(pageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Get top rated products successfully",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getRecentProducts(pageable: Pageable): PageResponse<ProductPort.ProductResponse> {
        log.info { "Fetching recent products" }
        
        val pageResult = productRepository.findRecentProducts(pageable)
        val productResponses = pageResult.content.map { it.toProductResponse() }

        return PageResponse(
            message = "Get recent products successfully",
            data = productResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getLowStockProducts(threshold: Int): List<ProductPort.ProductResponse> {
        log.info { "Fetching low stock products with threshold: $threshold" }
        
        val products = productRepository.findLowStockProducts(threshold)
        return products.map { it.toProductResponse() }
    }

    @Transactional(readOnly = true)
    override fun isProductAvailable(id: Long): Boolean {
        return productRepository.isProductAvailable(id)
    }

    override fun updateRating(id: Long, rating: Double): ProductPort.ProductResponse {
        log.info { "Updating rating for product ID: $id with rating: $rating" }
        
        val product = productRepository.findById(id)
            .orElseThrow { DataNotFoundException("Product with ID $id not found") }

        product.updateRating(rating)
        val updatedProduct = productRepository.save(product)
        
        log.info { "Rating updated successfully for product ID: $id" }
        return updatedProduct.toProductResponse()
    }

    override fun incrementSoldCount(id: Long, quantity: Int): ProductPort.ProductResponse {
        log.info { "Incrementing sold count for product ID: $id with quantity: $quantity" }
        
        val product = productRepository.findById(id)
            .orElseThrow { DataNotFoundException("Product with ID $id not found") }

        product.soldCount += quantity
        val updatedProduct = productRepository.save(product)
        
        log.info { "Sold count updated successfully for product ID: $id" }
        return updatedProduct.toProductResponse()
    }

    @Transactional(readOnly = true)
    override fun getProductSummary(): ProductPort.ProductSummaryResponse {
        log.info { "Generating product summary" }
        
        val totalProducts = productRepository.count()
        val activeProducts = productRepository.countByStatus(Product.ProductStatus.ACTIVE)
        val outOfStockProducts = productRepository.countOutOfStock()
        val averagePrice = productRepository.getAveragePrice() ?: 0.0
        val totalValue = productRepository.getTotalInventoryValue() ?: 0.0

        return ProductPort.ProductSummaryResponse(
            totalProducts = totalProducts,
            activeProducts = activeProducts,
            outOfStockProducts = outOfStockProducts,
            averagePrice = averagePrice,
            totalValue = totalValue
        )
    }

    @Transactional(readOnly = true)
    override fun getProductCountByCategory(categoryId: Long): Long {
        return productRepository.countByCategory(categoryId)
    }

    @Transactional(readOnly = true)
    override fun getProductCountByShop(shopId: Long): Long {
        return productRepository.countByShop(shopId)
    }
}
