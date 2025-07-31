package com.lcaohoanq.sp.domains.product

import com.lcaohoanq.sp.apis.MyApiResponseV2
import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.exceptions.MethodArgumentNotValidException
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.Sortable
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.prefix}/products")
@Tag(name = "products", description = "🛍️ Product API - Manage products in the system")
class ProductController(
    private val productService: IProductService
) : BaseController() {

    @Operation(
        summary = "Create a new product",
        description = "Create a new product with the provided details. Requires ADMIN or SHOP_OWNER role."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Product created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "403", description = "Access denied"),
            ApiResponse(responseCode = "404", description = "Category not found")
        ]
    )
    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_OWNER')")
    fun createProduct(
        @Valid @RequestBody request: ProductPort.ProductRequest,
        bindingResult: BindingResult
    ): ResponseEntity<MyApiResponseV2<ProductPort.ProductResponse>> {
        if (bindingResult.hasErrors()) throw MethodArgumentNotValidException(bindingResult)
        
        val response = productService.createProduct(request)
        return created(response)
    }

    @Operation(
        summary = "Get product by ID",
        description = "Retrieve a specific product by its ID"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product found"),
            ApiResponse(responseCode = "404", description = "Product not found")
        ]
    )
    @GetMapping("/{id}")
    fun getProductById(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<MyApiResponseV2<ProductPort.ProductResponse>> {
        val response = productService.getProductById(id)
        return ok("Get product successfully", response)
    }

    @Operation(
        summary = "Get all products",
        description = "Retrieve all products without pagination"
    )
    @GetMapping("/all")
    fun getAllProducts(): ResponseEntity<MyApiResponseV2<List<ProductPort.ProductResponse>>> {
        val response = productService.getAllProducts()
        return ok("Get all products successfully", response)
    }

    @Operation(
        summary = "Get paginated products",
        description = "Retrieve a paginated list of products with optional search and sorting"
    )
    @GetMapping("")
    fun getProductsPaged(
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int,
        
        @Parameter(description = "Search term for product name or description")
        @RequestParam(required = false, defaultValue = "") search: String,
        
        @Parameter(description = "Sort field")
        @RequestParam(required = false, defaultValue = "ID") sortBy: Sortable.ProductSortField,
        
        @Parameter(description = "Sort order")
        @RequestParam(required = false, defaultValue = "ASC") sortOrder: SortOrder
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val queryCriteria = QueryCriteria(search, sortBy, sortOrder)
        
        return ResponseEntity.ok(productService.getAllProducts(pageable, queryCriteria))
    }

    @Operation(
        summary = "Update product",
        description = "Update an existing product. Requires ADMIN or SHOP_OWNER role."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input data"),
            ApiResponse(responseCode = "403", description = "Access denied"),
            ApiResponse(responseCode = "404", description = "Product not found")
        ]
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_OWNER')")
    fun updateProduct(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long,
        @Valid @RequestBody request: ProductPort.ProductUpdateRequest,
        bindingResult: BindingResult
    ): ResponseEntity<MyApiResponseV2<ProductPort.ProductResponse>> {
        if (bindingResult.hasErrors()) throw MethodArgumentNotValidException(bindingResult)
        
        val response = productService.updateProduct(id, request)
        return updated(response)
    }

    @Operation(
        summary = "Delete product",
        description = "Delete a product by ID. Requires ADMIN role."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            ApiResponse(responseCode = "403", description = "Access denied"),
            ApiResponse(responseCode = "404", description = "Product not found")
        ]
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteProduct(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<MyApiResponseV2<Nothing?>> {
        productService.deleteProduct(id)
        return noContent()
    }

    @Operation(
        summary = "Update product stock",
        description = "Update product stock quantity. Requires ADMIN or SHOP_OWNER role."
    )
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_OWNER')")
    fun updateStock(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long,
        @Valid @RequestBody request: ProductPort.ProductStockUpdateRequest,
        bindingResult: BindingResult
    ): ResponseEntity<MyApiResponseV2<ProductPort.ProductResponse>> {
        if (bindingResult.hasErrors()) throw MethodArgumentNotValidException(bindingResult)
        
        val response = productService.updateStock(id, request)
        return ok("Stock updated successfully", response)
    }

    @Operation(
        summary = "Check product stock",
        description = "Get current stock quantity for a product"
    )
    @GetMapping("/{id}/stock")
    fun checkStock(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<MyApiResponseV2<Int>> {
        val stock = productService.checkStock(id)
        return ok("Stock retrieved successfully", stock)
    }

    @Operation(
        summary = "Search products",
        description = "Advanced search products with multiple filters"
    )
    @PostMapping("/search")
    fun searchProducts(
        @RequestBody request: ProductPort.ProductSearchRequest,
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val response = productService.searchProducts(request, pageable)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Search products by name",
        description = "Search products by name with pagination"
    )
    @GetMapping("/search/name")
    fun searchProductsByName(
        @Parameter(description = "Product name to search for")
        @RequestParam name: String,
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val response = productService.searchProductsByName(name, pageable)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Get products by category",
        description = "Retrieve products belonging to a specific category"
    )
    @GetMapping("/category/{categoryId}")
    fun getProductsByCategory(
        @Parameter(description = "Category ID", required = true)
        @PathVariable categoryId: Long,
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val response = productService.getProductsByCategory(categoryId, pageable)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Get products by shop",
        description = "Retrieve products belonging to a specific shop"
    )
    @GetMapping("/shop/{shopId}")
    fun getProductsByShop(
        @Parameter(description = "Shop ID", required = true)
        @PathVariable shopId: Long,
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val response = productService.getProductsByShop(shopId, pageable)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Get products by price range",
        description = "Retrieve products within a specific price range"
    )
    @GetMapping("/price-range")
    fun getProductsByPriceRange(
        @Parameter(description = "Minimum price")
        @RequestParam minPrice: Double,
        @Parameter(description = "Maximum price")
        @RequestParam maxPrice: Double,
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val response = productService.getProductsByPriceRange(minPrice, maxPrice, pageable)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Activate product",
        description = "Set product status to ACTIVE. Requires ADMIN or SHOP_OWNER role."
    )
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_OWNER')")
    fun activateProduct(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<MyApiResponseV2<ProductPort.ProductResponse>> {
        val response = productService.activateProduct(id)
        return ok("Product activated successfully", response)
    }

    @Operation(
        summary = "Deactivate product",
        description = "Set product status to INACTIVE. Requires ADMIN or SHOP_OWNER role."
    )
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_OWNER')")
    fun deactivateProduct(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<MyApiResponseV2<ProductPort.ProductResponse>> {
        val response = productService.deactivateProduct(id)
        return ok("Product deactivated successfully", response)
    }

    @Operation(
        summary = "Set product status",
        description = "Set product status to a specific value. Requires ADMIN or SHOP_OWNER role."
    )
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_OWNER')")
    fun setProductStatus(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long,
        @Parameter(description = "New product status")
        @RequestParam status: Product.ProductStatus
    ): ResponseEntity<MyApiResponseV2<ProductPort.ProductResponse>> {
        val response = productService.setProductStatus(id, status)
        return ok("Product status updated successfully", response)
    }

    @Operation(
        summary = "Get available products",
        description = "Retrieve products that are active and in stock"
    )
    @GetMapping("/available")
    fun getAvailableProducts(
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val response = productService.getAvailableProducts(pageable)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Get top selling products",
        description = "Retrieve products ordered by sold count descending"
    )
    @GetMapping("/top-selling")
    fun getTopSellingProducts(
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val response = productService.getTopSellingProducts(pageable)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Get top rated products",
        description = "Retrieve products ordered by rating descending"
    )
    @GetMapping("/top-rated")
    fun getTopRatedProducts(
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val response = productService.getTopRatedProducts(pageable)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Get recent products",
        description = "Retrieve recently added products ordered by creation date descending"
    )
    @GetMapping("/recent")
    fun getRecentProducts(
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(required = false, defaultValue = "0") page: Int,
        
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ResponseEntity<PageResponse<ProductPort.ProductResponse>> {
        val pageable = PageRequest.of(page, limit)
        val response = productService.getRecentProducts(pageable)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Get low stock products",
        description = "Retrieve products with stock below threshold. Requires ADMIN or SHOP_OWNER role."
    )
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SHOP_OWNER')")
    fun getLowStockProducts(
        @Parameter(description = "Stock threshold", example = "10")
        @RequestParam(required = false, defaultValue = "10") threshold: Int
    ): ResponseEntity<MyApiResponseV2<List<ProductPort.ProductResponse>>> {
        val response = productService.getLowStockProducts(threshold)
        return ok("Low stock products retrieved successfully", response)
    }

    @Operation(
        summary = "Check if product is available",
        description = "Check if a product is active and in stock"
    )
    @GetMapping("/{id}/availability")
    fun isProductAvailable(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long
    ): ResponseEntity<MyApiResponseV2<Boolean>> {
        val available = productService.isProductAvailable(id)
        return ok("Product availability checked successfully", available)
    }

    @Operation(
        summary = "Update product rating",
        description = "Update product rating based on reviews. Usually called internally."
    )
    @PatchMapping("/{id}/rating")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER')")
    fun updateRating(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long,
        @Parameter(description = "Rating value (0-5)")
        @RequestParam rating: Double
    ): ResponseEntity<MyApiResponseV2<ProductPort.ProductResponse>> {
        val response = productService.updateRating(id, rating)
        return ok("Rating updated successfully", response)
    }

    @Operation(
        summary = "Increment sold count",
        description = "Increment product sold count. Usually called when order is completed."
    )
    @PatchMapping("/{id}/sold-count")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SYSTEM')")
    fun incrementSoldCount(
        @Parameter(description = "Product ID", required = true)
        @PathVariable id: Long,
        @Parameter(description = "Quantity sold")
        @RequestParam quantity: Int
    ): ResponseEntity<MyApiResponseV2<ProductPort.ProductResponse>> {
        val response = productService.incrementSoldCount(id, quantity)
        return ok("Sold count updated successfully", response)
    }

    @Operation(
        summary = "Get product statistics",
        description = "Get summary statistics for products. Requires ADMIN role."
    )
    @GetMapping("/summary")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getProductSummary(): ResponseEntity<MyApiResponseV2<ProductPort.ProductSummaryResponse>> {
        val response = productService.getProductSummary()
        return ok("Product summary retrieved successfully", response)
    }

    @Operation(
        summary = "Get product count by category",
        description = "Get number of products in a specific category"
    )
    @GetMapping("/count/category/{categoryId}")
    fun getProductCountByCategory(
        @Parameter(description = "Category ID", required = true)
        @PathVariable categoryId: Long
    ): ResponseEntity<MyApiResponseV2<Long>> {
        val count = productService.getProductCountByCategory(categoryId)
        return ok("Product count by category retrieved successfully", count)
    }

    @Operation(
        summary = "Get product count by shop",
        description = "Get number of products in a specific shop"
    )
    @GetMapping("/count/shop/{shopId}")
    fun getProductCountByShop(
        @Parameter(description = "Shop ID", required = true)
        @PathVariable shopId: Long
    ): ResponseEntity<MyApiResponseV2<Long>> {
        val count = productService.getProductCountByShop(shopId)
        return ok("Product count by shop retrieved successfully", count)
    }
}
