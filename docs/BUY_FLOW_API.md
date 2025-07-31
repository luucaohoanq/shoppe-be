# Buy Flow API Documentation

This document provides comprehensive documentation for the complete product purchase flow in the Shoppe e-commerce application.

## Overview

The buy flow consists of the following main steps:

1. Browse and search products
2. Add products to cart
3. Manage cart items
4. Checkout from cart
5. Create order
6. Process payment
7. Track order status

## API Endpoints

### 1. Product Management

#### Browse Products

```http
GET /api/v1/products
```

**Query Parameters:**

- `page` (default: 0) - Page number
- `size` (default: 10) - Page size
- `sortBy` (default: "createdAt") - Sort field
- `sortDirection` (default: "desc") - Sort direction
- `categoryId` - Filter by category
- `minPrice` - Minimum price filter
- `maxPrice` - Maximum price filter
- `name` - Search by product name

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Products retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Product Name",
      "description": "Product description",
      "price": 99.99,
      "stock": 100,
      "imageUrl": "https://example.com/image.jpg",
      "categoryId": 1,
      "shopId": 1,
      "status": "ACTIVE",
      "averageRating": 4.5,
      "totalReviews": 25,
      "createdAt": "2025-06-22T10:00:00",
      "updatedAt": "2025-06-22T10:00:00"
    }
  ],
  "paginationMeta": {
    "totalPages": 10,
    "totalItems": 100,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

#### Get Product Details

```http
GET /api/v1/products/{productId}
```

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "name": "Product Name",
    "description": "Detailed product description",
    "price": 99.99,
    "stock": 100,
    "imageUrl": "https://example.com/image.jpg",
    "categoryId": 1,
    "shopId": 1,
    "status": "ACTIVE",
    "averageRating": 4.5,
    "totalReviews": 25,
    "createdAt": "2025-06-22T10:00:00",
    "updatedAt": "2025-06-22T10:00:00"
  }
}
```

#### Search Products

```http
POST /api/v1/products/search
```

**Request Body:**

```json
{
  "query": "search term",
  "categoryIds": [1, 2],
  "minPrice": 10.0,
  "maxPrice": 100.0,
  "sortBy": "PRICE_ASC",
  "inStock": true
}
```

### 2. Cart Management

#### Get Cart

```http
GET /api/v1/carts
```

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Cart retrieved successfully",
  "data": {
    "id": 1,
    "userId": 123,
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Product Name",
        "productImage": "https://example.com/image.jpg",
        "quantity": 2,
        "unitPrice": 99.99,
        "totalPrice": 199.98
      }
    ],
    "totalItems": 2,
    "subtotal": 199.98,
    "isEmpty": false,
    "createdAt": "2025-06-22T10:00:00",
    "updatedAt": "2025-06-22T10:00:00"
  }
}
```

#### Add Item to Cart

```http
POST /api/v1/carts/items
```

**Request Body:**

```json
{
  "productId": 1,
  "quantity": 2
}
```

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Item added to cart successfully",
  "data": {
    "id": 1,
    "productId": 1,
    "productName": "Product Name",
    "productImage": "https://example.com/image.jpg",
    "quantity": 2,
    "unitPrice": 99.99,
    "totalPrice": 199.98
  }
}
```

#### Update Cart Item

```http
PUT /api/v1/carts/items/{productId}
```

**Request Body:**

```json
{
  "quantity": 3
}
```

#### Remove Cart Item

```http
DELETE /api/v1/carts/items/{productId}
```

#### Clear Cart

```http
DELETE /api/v1/carts
```

#### Validate Cart

```http
GET /api/v1/carts/validate
```

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Cart validation completed",
  "data": {
    "isValid": true,
    "hasUnavailableItems": false,
    "hasInsufficientStock": false,
    "unavailableItems": [],
    "insufficientStockItems": [],
    "totalValidItems": 2,
    "totalValidAmount": 199.98
  }
}
```

### 3. Order Management

#### Create Order

```http
POST /api/v1/orders
```

**Request Body:**

```json
{
  "addressId": 1,
  "shippingMethodId": 1,
  "couponId": null,
  "notes": "Please deliver after 6 PM",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Order created successfully",
  "data": {
    "id": 1,
    "orderNumber": "ORD-20250622100000-1234",
    "userId": 123,
    "status": "PENDING",
    "paymentStatus": "PENDING",
    "shippingStatus": "NOT_SHIPPED",
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Product Name",
        "productImage": "https://example.com/image.jpg",
        "quantity": 2,
        "unitPrice": 99.99,
        "totalPrice": 199.98
      }
    ],
    "subtotal": 199.98,
    "shippingFee": 10.0,
    "discountAmount": 0.0,
    "totalAmount": 209.98,
    "addressId": 1,
    "shippingMethodId": 1,
    "couponId": null,
    "notes": "Please deliver after 6 PM",
    "estimatedDelivery": "2025-06-25T10:00:00",
    "createdAt": "2025-06-22T10:00:00",
    "updatedAt": "2025-06-22T10:00:00"
  }
}
```

#### Checkout from Cart

```http
POST /api/v1/orders/checkout
```

**Request Body:**

```json
{
  "addressId": 1,
  "shippingMethodId": 1,
  "couponId": null,
  "paymentMethod": "CREDIT_CARD",
  "notes": "Please deliver after 6 PM",
  "useCartItems": true,
  "selectedCartItems": [1, 2]
}
```

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Checkout completed successfully",
  "data": {
    "order": {
      "id": 1,
      "orderNumber": "ORD-20250622100000-1234",
      "totalAmount": 209.98,
      "status": "PENDING"
    },
    "paymentUrl": "https://payment-gateway.com/cc/TXN-20250622100000-12345",
    "paymentInstructions": "Please complete payment using your credit card details."
  }
}
```

#### Get Order Details

```http
GET /api/v1/orders/{orderId}
```

#### Get Order by Number

```http
GET /api/v1/orders/number/{orderNumber}
```

#### Get User Orders

```http
GET /api/v1/orders/my-orders
```

**Query Parameters:**

- `page` (default: 0)
- `size` (default: 10)
- `sortBy` (default: "createdAt")
- `sortDirection` (default: "desc")

#### Cancel Order

```http
PUT /api/v1/orders/{orderId}/cancel?reason=Changed my mind
```

#### Search User Orders

```http
POST /api/v1/orders/search/my-orders
```

**Request Body:**

```json
{
  "orderNumber": "ORD-2025",
  "status": "PENDING",
  "startDate": "2025-06-01T00:00:00",
  "endDate": "2025-06-30T23:59:59",
  "minAmount": 50.0,
  "maxAmount": 500.0
}
```

### 4. Payment Management

#### Get Payment by Order

```http
GET /api/v1/payments/order/{orderId}
```

**Response:**

```json
{
  "status": "SUCCESS",
  "message": "Payment retrieved successfully",
  "data": {
    "id": 1,
    "orderId": 1,
    "amount": 209.98,
    "paymentMethod": "CREDIT_CARD",
    "status": "PENDING",
    "transactionId": "TXN-20250622100000-12345",
    "paymentUrl": "https://payment-gateway.com/cc/TXN-20250622100000-12345",
    "description": null,
    "createdAt": "2025-06-22T10:00:00",
    "updatedAt": "2025-06-22T10:00:00"
  }
}
```

#### Payment Callback (for payment gateways)

```http
POST /api/v1/payments/callback?transactionId=TXN-123&status=COMPLETED
```

### 5. Admin Endpoints

#### Order Management (Admin)

```http
GET /api/v1/orders/admin/all
PUT /api/v1/orders/admin/{orderId}/confirm
PUT /api/v1/orders/admin/{orderId}/ship?trackingNumber=TRACK123
PUT /api/v1/orders/admin/{orderId}/deliver
GET /api/v1/orders/admin/stats
GET /api/v1/orders/admin/pending
GET /api/v1/orders/admin/overdue
```

#### Payment Management (Admin)

```http
GET /api/v1/payments/admin/all
POST /api/v1/payments/admin/search
GET /api/v1/payments/admin/stats
GET /api/v1/payments/admin/failed
GET /api/v1/payments/admin/pending
POST /api/v1/payments/{paymentId}/refund
```

## Complete Buy Flow Example

### Step 1: Browse Products

```http
GET /api/v1/products?categoryId=1&minPrice=50&maxPrice=200
```

### Step 2: Add to Cart

```http
POST /api/v1/carts/items
{
  "productId": 1,
  "quantity": 2
}
```

### Step 3: Validate Cart

```http
GET /api/v1/carts/validate
```

### Step 4: Checkout

```http
POST /api/v1/orders/checkout
{
  "addressId": 1,
  "shippingMethodId": 1,
  "paymentMethod": "CREDIT_CARD",
  "useCartItems": true
}
```

### Step 5: Complete Payment

User follows the payment URL from checkout response

### Step 6: Track Order

```http
GET /api/v1/orders/my-orders
```

## Error Handling

All endpoints return consistent error responses:

```json
{
  "status": "ERROR",
  "message": "Error description",
  "data": null,
  "error": {
    "code": "ERROR_CODE",
    "details": "Detailed error information"
  }
}
```

Common error codes:

- `PRODUCT_NOT_FOUND` - Product not found
- `INSUFFICIENT_STOCK` - Not enough stock
- `CART_EMPTY` - Cart is empty
- `INVALID_PAYMENT_METHOD` - Invalid payment method
- `ORDER_NOT_FOUND` - Order not found
- `UNAUTHORIZED_ACCESS` - Access denied

## Status Definitions

### Order Status

- `PENDING` - Order placed, awaiting confirmation
- `CONFIRMED` - Order confirmed by admin
- `SHIPPED` - Order shipped
- `DELIVERED` - Order delivered
- `CANCELLED` - Order cancelled

### Payment Status

- `PENDING` - Payment awaiting processing
- `COMPLETED` - Payment successful
- `FAILED` - Payment failed
- `REFUNDED` - Payment refunded
- `CANCELLED` - Payment cancelled

### Shipping Status

- `NOT_SHIPPED` - Not yet shipped
- `SHIPPED` - Package shipped
- `IN_TRANSIT` - Package in transit
- `DELIVERED` - Package delivered
- `RETURNED` - Package returned

## Authentication

All endpoints require authentication using JWT tokens:

```http
Authorization: Bearer <jwt_token>
```

Admin endpoints require ADMIN or MANAGER role.
