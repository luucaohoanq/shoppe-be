package com.lcaohoanq.sp.domains.product

import com.lcaohoanq.sp.domains.categories.CategoryPort
import com.lcaohoanq.sp.utils.Sortable
import jakarta.validation.constraints.*
import java.time.LocalDateTime

object ProductPort {

    data class ProductRequest(
        @NotBlank(message = "Product name is required")
        @Size(min = 1, max = 200, message = "Product name must be between 1 and 200 characters")
        val name: String,

        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        val description: String = "",

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        @DecimalMax(value = "999999.99", message = "Price cannot exceed 999,999.99")
        val price: Double,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        @Max(value = 999999, message = "Stock cannot exceed 999,999")
        val stock: Int,

        @NotNull(message = "Category ID is required")
        @Positive(message = "Category ID must be positive")
        val categoryId: Long,

        @NotNull(message = "Shop ID is required")
        @Positive(message = "Shop ID must be positive")
        val shopId: Long,

        val imageUrl: String? = null,

        @DecimalMin(value = "0.0", inclusive = false, message = "Weight must be greater than 0")
        @DecimalMax(value = "9999.99", message = "Weight cannot exceed 9999.99")
        @PositiveOrZero(message = "Weight must be zero or positive")
        val weight: Double? = null,

        @Size(max = 100, message = "Dimensions cannot exceed 100 characters")
        val dimensions: String? = null,
    )

    data class ProductUpdateRequest(
        @Size(min = 1, max = 200, message = "Product name must be between 1 and 200 characters")
        val name: String?,

        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        val description: String?,

        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        @DecimalMax(value = "999999.99", message = "Price cannot exceed 999,999.99")
        val price: Double?,

        @Min(value = 0, message = "Stock cannot be negative")
        @Max(value = 999999, message = "Stock cannot exceed 999,999")
        val stock: Int?,

        @Positive(message = "Category ID must be positive")
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
        val shopId: Long,
        val imageUrl: String?,
        val status: Product.ProductStatus,
        val rating: Double,
        val reviewCount: Int,
        val soldCount: Int,
        val weight: Double?,
        val dimensions: String?,
        val sku: String? = null,
        val featured: Boolean? = false,
        val category: CategoryPort.CategoryRes?,
        val createdAt: LocalDateTime?,
        val updatedAt: LocalDateTime?,
        val isAvailable: Boolean = status == Product.ProductStatus.ACTIVE && stock > 0
    )

    data class ProductStockUpdateRequest(
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be positive")
        val quantity: Int,

        @NotBlank(message = "Operation type is required")
        @Pattern(regexp = "ADD|SUBTRACT", message = "Operation must be ADD or SUBTRACT")
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
