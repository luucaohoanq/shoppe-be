package com.lcaohoanq.sp.domains.product

import com.lcaohoanq.sp.domains.categories.CategoryPort
import com.lcaohoanq.sp.utils.Sortable
import jakarta.validation.constraints.*
import java.time.LocalDateTime

object ProductPort {

    data class ProductRequest(
        @field:NotBlank(message = "Product name is required")
        @field:Size(min = 1, max = 200, message = "Product name must be between 1 and 200 characters")
        val name: String,

        @field:Size(max = 2000, message = "Description cannot exceed 2000 characters")
        val description: String = "",

        @field:NotNull(message = "Price is required")
        @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        @field:DecimalMax(value = "999999.99", message = "Price cannot exceed 999,999.99")
        val price: Double,

        @field:NotNull(message = "Stock is required")
        @field:Min(value = 0, message = "Stock cannot be negative")
        @field:Max(value = 999999, message = "Stock cannot exceed 999,999")
        val stock: Int,

        @field:NotNull(message = "Category ID is required")
        @field:Positive(message = "Category ID must be positive")
        val categoryId: Long,

        @field:NotNull(message = "Shop ID is required")
        @field:Positive(message = "Shop ID must be positive")
        val shopId: Long,

        val imageUrl: String? = null,

        val weight: Double? = null,

        val dimensions: String? = null,

        val status: Product.ProductStatus = Product.ProductStatus.ACTIVE
    )

    data class ProductUpdateRequest(
        @field:Size(min = 1, max = 200, message = "Product name must be between 1 and 200 characters")
        val name: String?,

        @field:Size(max = 2000, message = "Description cannot exceed 2000 characters")
        val description: String?,

        @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        @field:DecimalMax(value = "999999.99", message = "Price cannot exceed 999,999.99")
        val price: Double?,

        @field:Min(value = 0, message = "Stock cannot be negative")
        @field:Max(value = 999999, message = "Stock cannot exceed 999,999")
        val stock: Int?,

        @field:Positive(message = "Category ID must be positive")
        val categoryId: Long?,

        val imageUrl: String?,

        val weight: Double?,

        val dimensions: String?,

        val status: Product.ProductStatus?
    )

    data class ProductResponse(
        val id: Long,
        val name: String,
        val description: String,
        val price: Double,
        val stock: Int,
        val categoryId: Long,
        val shopId: Long,
        val imageUrl: String?,
        val status: Product.ProductStatus,
        val rating: Double,
        val reviewCount: Int,
        val soldCount: Int,
        val weight: Double?,
        val dimensions: String?,
        val category: CategoryPort.CategoryRes?,
        val createdAt: LocalDateTime?,
        val updatedAt: LocalDateTime?,
        val isAvailable: Boolean = status == Product.ProductStatus.ACTIVE && stock > 0
    )

    data class ProductStockUpdateRequest(
        @field:NotNull(message = "Quantity is required")
        @field:Min(value = 1, message = "Quantity must be positive")
        val quantity: Int,

        @field:NotBlank(message = "Operation type is required")
        @field:Pattern(regexp = "ADD|SUBTRACT", message = "Operation must be ADD or SUBTRACT")
        val operation: String
    )

    data class ProductSearchRequest(
        val name: String? = null,
        val categoryId: Long? = null,
        val shopId: Long? = null,
        val minPrice: Double? = null,
        val maxPrice: Double? = null,
        val status: Product.ProductStatus? = null,
        val inStock: Boolean? = null,
        val sortBy: Sortable.ProductSortField = Sortable.ProductSortField.ID,
        val sortOrder: String = "ASC"
    )

    data class ProductSummaryResponse(
        val totalProducts: Long,
        val activeProducts: Long,
        val outOfStockProducts: Long,
        val averagePrice: Double,
        val totalValue: Double
    )
}
