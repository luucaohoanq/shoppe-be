# Product API Documentation

## Overview

The Product API provides comprehensive functionality for managing products in the e-commerce system. It includes operations for creating, reading, updating, and deleting products, as well as advanced features like stock management, search, filtering, and analytics.

## Architecture

The Product domain follows the Clean Architecture pattern with the following layers:

```
Product Domain
├── Product.kt              # Entity
├── ProductPort.kt          # DTOs and Interfaces
├── ProductRepository.kt    # Repository Interface
├── IProductService.kt      # Service Interface
├── ProductService.kt       # Service Implementation
├── ProductController.kt    # REST Controller
├── ProductSpecification.kt # JPA Specifications for filtering
└── ProductExtensions.kt    # Extension functions for conversions
```

## Features

### 🛍️ Product Management

- **CRUD Operations**: Create, read, update, delete products
- **Batch Operations**: Support for bulk operations
- **Status Management**: Activate/deactivate products
- **Category Integration**: Products are linked to categories

### 📦 Stock Management

- **Stock Updates**: Add or subtract stock quantities
- **Stock Monitoring**: Check current stock levels
- **Low Stock Alerts**: Identify products with low inventory
- **Automatic Status Updates**: Products go out of stock automatically

### 🔍 Search & Filtering

- **Text Search**: Search by product name and description
- **Advanced Filters**: Filter by category, shop, price range, status
- **Sorting**: Sort by various fields (name, price, rating, etc.)
- **Pagination**: Efficient pagination for large datasets

### 📊 Analytics & Reporting

- **Product Statistics**: Total products, active products, inventory value
- **Performance Metrics**: Top selling, top rated, recent products
- **Category Analytics**: Product count by category
- **Shop Analytics**: Product count by shop

## API Endpoints

### Product CRUD

| Method | Endpoint                | Description            | Auth Required    |
| ------ | ----------------------- | ---------------------- | ---------------- |
| POST   | `/api/v1/products`      | Create new product     | Admin/Shop Owner |
| GET    | `/api/v1/products/{id}` | Get product by ID      | Public           |
| GET    | `/api/v1/products`      | Get paginated products | Public           |
| GET    | `/api/v1/products/all`  | Get all products       | Public           |
| PUT    | `/api/v1/products/{id}` | Update product         | Admin/Shop Owner |
| DELETE | `/api/v1/products/{id}` | Delete product         | Admin            |

### Stock Management

| Method | Endpoint                      | Description            | Auth Required    |
| ------ | ----------------------------- | ---------------------- | ---------------- |
| PATCH  | `/api/v1/products/{id}/stock` | Update stock           | Admin/Shop Owner |
| GET    | `/api/v1/products/{id}/stock` | Check stock            | Public           |
| GET    | `/api/v1/products/low-stock`  | Get low stock products | Admin/Shop Owner |

### Search & Filtering

| Method | Endpoint                         | Description        | Auth Required |
| ------ | -------------------------------- | ------------------ | ------------- |
| POST   | `/api/v1/products/search`        | Advanced search    | Public        |
| GET    | `/api/v1/products/search/name`   | Search by name     | Public        |
| GET    | `/api/v1/products/category/{id}` | Get by category    | Public        |
| GET    | `/api/v1/products/shop/{id}`     | Get by shop        | Public        |
| GET    | `/api/v1/products/price-range`   | Get by price range | Public        |

### Status Management

| Method | Endpoint                           | Description         | Auth Required    |
| ------ | ---------------------------------- | ------------------- | ---------------- |
| PATCH  | `/api/v1/products/{id}/activate`   | Activate product    | Admin/Shop Owner |
| PATCH  | `/api/v1/products/{id}/deactivate` | Deactivate product  | Admin/Shop Owner |
| PATCH  | `/api/v1/products/{id}/status`     | Set specific status | Admin/Shop Owner |

### Special Queries

| Method | Endpoint                       | Description            | Auth Required |
| ------ | ------------------------------ | ---------------------- | ------------- |
| GET    | `/api/v1/products/available`   | Get available products | Public        |
| GET    | `/api/v1/products/top-selling` | Get top selling        | Public        |
| GET    | `/api/v1/products/top-rated`   | Get top rated          | Public        |
| GET    | `/api/v1/products/recent`      | Get recent products    | Public        |

### Analytics

| Method | Endpoint                               | Description            | Auth Required |
| ------ | -------------------------------------- | ---------------------- | ------------- |
| GET    | `/api/v1/products/summary`             | Get product statistics | Admin         |
| GET    | `/api/v1/products/count/category/{id}` | Count by category      | Public        |
| GET    | `/api/v1/products/count/shop/{id}`     | Count by shop          | Public        |

### Utility

| Method | Endpoint                             | Description        | Auth Required |
| ------ | ------------------------------------ | ------------------ | ------------- |
| GET    | `/api/v1/products/{id}/availability` | Check availability | Public        |
| PATCH  | `/api/v1/products/{id}/rating`       | Update rating      | Member        |
| PATCH  | `/api/v1/products/{id}/sold-count`   | Update sold count  | System        |

## Data Models

### Product Entity

```kotlin
@Entity
class Product(
    val id: Long?,
    var name: String,
    var description: String,
    var price: Double,
    var stock: Int,
    var categoryId: Long,
    var shopId: Long,
    var imageUrl: String?,
    var status: ProductStatus,
    var rating: Double,
    var reviewCount: Int,
    var soldCount: Int,
    var weight: Double?,
    var dimensions: String?
) : BaseEntity()

enum class ProductStatus {
    ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED
}
```

### DTOs

#### ProductRequest

```kotlin
data class ProductRequest(
    @NotBlank val name: String,
    val description: String = "",
    @NotNull @DecimalMin("0.0") val price: Double,
    @NotNull @Min(0) val stock: Int,
    @NotNull @Positive val categoryId: Long,
    @NotNull @Positive val shopId: Long,
    val imageUrl: String? = null,
    val weight: Double? = null,
    val dimensions: String? = null,
    val status: ProductStatus = ProductStatus.ACTIVE
)
```

#### ProductResponse

```kotlin
data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val categoryId: Long,
    val shopId: Long,
    val imageUrl: String?,
    val status: ProductStatus,
    val rating: Double,
    val reviewCount: Int,
    val soldCount: Int,
    val weight: Double?,
    val dimensions: String?,
    val category: CategoryResponse?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val isAvailable: Boolean
)
```

#### ProductSearchRequest

```kotlin
data class ProductSearchRequest(
    val name: String? = null,
    val categoryId: Long? = null,
    val shopId: Long? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val status: ProductStatus? = null,
    val inStock: Boolean? = null,
    val sortBy: ProductSortField = ProductSortField.ID,
    val sortOrder: String = "ASC"
)
```

#### StockUpdateRequest

```kotlin
data class ProductStockUpdateRequest(
    @NotNull @Min(1) val quantity: Int,
    @NotBlank @Pattern(regexp = "ADD|SUBTRACT") val operation: String
)
```

## Usage Examples

### Create a Product

```kotlin
// Request
POST /api/v1/products
{
    "name": "Smartphone X Pro",
    "description": "Latest smartphone with advanced features",
    "price": 999.99,
    "stock": 50,
    "categoryId": 1,
    "shopId": 1,
    "imageUrl": "https://example.com/image.jpg",
    "weight": 0.2,
    "dimensions": "15x7x0.8cm"
}

// Response
{
    "success": true,
    "message": "Product created successfully",
    "data": {
        "id": 1,
        "name": "Smartphone X Pro",
        "description": "Latest smartphone with advanced features",
        "price": 999.99,
        "stock": 50,
        "categoryId": 1,
        "shopId": 1,
        "imageUrl": "https://example.com/image.jpg",
        "status": "ACTIVE",
        "rating": 0.0,
        "reviewCount": 0,
        "soldCount": 0,
        "weight": 0.2,
        "dimensions": "15x7x0.8cm",
        "category": {...},
        "createdAt": "2024-01-01T10:00:00",
        "updatedAt": "2024-01-01T10:00:00",
        "isAvailable": true
    }
}
```

### Search Products

```kotlin
// Request
POST /api/v1/products/search?page=0&limit=10
{
    "name": "smartphone",
    "categoryId": 1,
    "minPrice": 500.0,
    "maxPrice": 1500.0,
    "status": "ACTIVE",
    "inStock": true,
    "sortBy": "PRICE",
    "sortOrder": "ASC"
}

// Response
{
    "message": "Search products successfully",
    "data": [...],
    "paginationMeta": {
        "totalPages": 5,
        "totalItems": 47,
        "currentPage": 0,
        "pageSize": 10
    }
}
```

### Update Stock

```kotlin
// Request
PATCH /api/v1/products/1/stock
{
    "quantity": 10,
    "operation": "ADD"
}

// Response
{
    "success": true,
    "message": "Stock updated successfully",
    "data": {
        "id": 1,
        "stock": 60,
        ...
    }
}
```

## Validation Rules

### Product Creation

- **Name**: Required, 1-200 characters
- **Description**: Optional, max 2000 characters
- **Price**: Required, > 0, max 999,999.99
- **Stock**: Required, ≥ 0, max 999,999
- **Category ID**: Required, must exist
- **Shop ID**: Required, positive number

### Stock Operations

- **Quantity**: Required, positive number
- **Operation**: Required, must be "ADD" or "SUBTRACT"

## Error Handling

### Common Error Responses

```kotlin
// Product Not Found
{
    "success": false,
    "message": "Product with ID 999 not found",
    "statusCode": 404
}

// Validation Error
{
    "success": false,
    "message": "Validation failed",
    "errors": {
        "name": "Product name is required",
        "price": "Price must be greater than 0"
    },
    "statusCode": 400
}

// Access Denied
{
    "success": false,
    "message": "Access denied",
    "statusCode": 403
}
```

## Performance Considerations

### Database Indexing

- Primary key: `id`
- Foreign keys: `category_id`, `shop_id`
- Search fields: `name`, `status`, `price`
- Composite indexes for common filter combinations

### Caching Strategy

- Product details cached for 15 minutes
- Search results cached for 5 minutes
- Category-based queries cached for 30 minutes

### Pagination

- Default page size: 10 items
- Maximum page size: 100 items
- Uses offset-based pagination for simplicity

## Security

### Authentication

- **Public endpoints**: Product viewing, search
- **Member required**: Rating updates
- **Shop Owner required**: Stock management, product updates
- **Admin required**: Product deletion, statistics

### Authorization

- Shop owners can only manage their own products
- Admin users have full access
- Cross-shop operations require admin privileges

## Testing

The implementation includes comprehensive unit tests covering:

- ✅ Product CRUD operations
- ✅ Stock management
- ✅ Search and filtering
- ✅ Error handling
- ✅ Validation
- ✅ Business logic

Run tests with:

```bash
./mvnw test -Dtest=ProductServiceTest
```

## Migration from Old Entity

If you're migrating from the old `entities/Product.kt`, follow these steps:

1. **Update imports** in existing code to use `domains.product.Product`
2. **Update field types**: `Int` ID → `Long` ID
3. **Add new fields**: `rating`, `reviewCount`, `soldCount`, etc.
4. **Update relationships**: Add proper JPA relationships
5. **Run database migration** to add new columns

## Future Enhancements

- [ ] Product variants (size, color, etc.)
- [ ] Bulk operations API
- [ ] Product comparison API
- [ ] Advanced analytics dashboard
- [ ] Real-time stock notifications
- [ ] Product recommendations
- [ ] Multi-language support
- [ ] Product image management

This Product API provides a solid foundation for e-commerce product management with room for future expansion and customization.
