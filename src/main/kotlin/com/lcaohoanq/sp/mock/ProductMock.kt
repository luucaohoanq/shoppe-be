package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.domains.categories.Category
import com.lcaohoanq.sp.domains.product.Product
import com.lcaohoanq.sp.repositories.ProductRepository

fun initProducts(productRepository: ProductRepository, categories: List<Category>) {
    val products = mutableListOf<Product>()

    // Electronics products
    val electronicsCategory = categories.find { it.name == "Electronics" }
    electronicsCategory?.let {
        products.add(
            Product(
                name = "Smartphone X",
                description = "Latest smartphone with advanced features",
                price = 799.99,
                rating = 5.0,
                stock = 50,
                soldCount = 245, // Add sold count
                imageUrl = "https://example.com/images/smartphone-x.jpg",
                images = listOf(
                    "https://api-ecom.duthanhduoc.com/images/08b79b1d-169d-4de1-85a2-4e5e8ff535b7.jpg",
                    "https://api-ecom.duthanhduoc.com/images/182d6e25-65fa-4abe-b822-70d87550bf4e.jpg",
                    "https://api-ecom.duthanhduoc.com/images/827e675d-e553-497e-9b15-d2df5fc7192d.jpg",
                    "https://api-ecom.duthanhduoc.com/images/b6425e3f-3cc3-4696-94f7-5053afca2c71.jpg",
                    "https://api-ecom.duthanhduoc.com/images/4d80b312-e605-4508-ab80-14dd75f6d23d.jpg",
                    "https://api-ecom.duthanhduoc.com/images/9e628716-0b94-44d8-850c-e96adc4b1c8f.jpg",
                    "https://api-ecom.duthanhduoc.com/images/20a1a8e5-1b49-4854-a221-0f96130b5fd8.jpg",
                ),
                sku = "ELEC-SP-001",
                weight = 0.2,
                dimensions = "15x7x1 cm",
                featured = true,
            )
        )
        products.add(
            Product(
                name = "Laptop Pro",
                description = "High-performance laptop for professionals",
                price = 1299.99,
                stock = 30,
                soldCount = 89, // Add sold count
                imageUrl = "https://example.com/images/laptop-pro.jpg",
                sku = "ELEC-LP-002",
                weight = 2.0,
                dimensions = "35x25x2 cm",
                featured = true,
            )
        )
        products.add(
            Product(
                name = "Wireless Earbuds",
                description = "Premium wireless earbuds with noise cancellation",
                price = 149.99,
                stock = 100,
                soldCount = 187, // Add sold count
                imageUrl = "https://example.com/images/wireless-earbuds.jpg",
                sku = "ELEC-WE-003",
                weight = 0.05,
                dimensions = "5x5x3 cm",
                featured = false,
            )
        )
    }

    // Clothing products
    val clothingCategory = categories.find { it.name == "Clothing" }
    clothingCategory?.let {
        products.add(
            Product(
                name = "Men's Casual Shirt",
                description = "Comfortable cotton casual shirt for men",
                price = 39.99,
                stock = 200,
                soldCount = 324, // Add sold count
                imageUrl = "https://example.com/images/mens-shirt.jpg",
                sku = "CLOTH-MS-001",
                weight = 0.3,
                dimensions = "30x20x2 cm",
                featured = false,
            )
        )
        products.add(
            Product(
                name = "Women's Dress",
                description = "Elegant dress for women",
                price = 59.99,
                stock = 150,
                soldCount = 156, // Add sold count
                imageUrl = "https://example.com/images/womens-dress.jpg",
                sku = "CLOTH-WD-002",
                weight = 0.4,
                dimensions = "40x30x2 cm",
                featured = true,
            )
        )
    }

    // Home & Kitchen products
    val homeCategory = categories.find { it.name == "Home & Kitchen" }
    homeCategory?.let {
        products.add(
            Product(
                name = "Coffee Maker",
                description = "Automatic coffee maker with timer",
                price = 89.99,
                stock = 75,
                soldCount = 67, // Add sold count
                imageUrl = "https://example.com/images/coffee-maker.jpg",
                sku = "HOME-CM-001",
                weight = 3.0,
                dimensions = "25x20x30 cm",
                featured = false,
            )
        )
        products.add(
            Product(
                name = "Blender",
                description = "High-speed blender for smoothies and more",
                price = 69.99,
                stock = 60,
                soldCount = 43, // Add sold count
                imageUrl = "https://example.com/images/blender.jpg",
                sku = "HOME-BL-002",
                weight = 2.5,
                dimensions = "20x15x35 cm",
                featured = false,
            )
        )
    }

    // Books products
    val booksCategory = categories.find { it.name == "Books" }
    booksCategory?.let {
        products.add(
            Product(
                name = "The Great Novel",
                description = "Bestselling fiction novel",
                price = 14.99,
                stock = 300,
                soldCount = 512, // Add sold count
                imageUrl = "https://example.com/images/great-novel.jpg",
                sku = "BOOK-GN-001",
                weight = 0.5,
                dimensions = "20x15x3 cm",
                featured = false,
            )
        )
        products.add(
            Product(
                name = "Cooking Masterclass",
                description = "Comprehensive cookbook for all skill levels",
                price = 24.99,
                stock = 150,
                soldCount = 89, // Add sold count
                imageUrl = "https://example.com/images/cookbook.jpg",
                sku = "BOOK-CM-002",
                weight = 0.8,
                dimensions = "25x20x2 cm",
                featured = true,
            )
        )
    }

    // Beauty products
    val beautyCategory = categories.find { it.name == "Beauty & Personal Care" }
    beautyCategory?.let {
        products.add(
            Product(
                name = "Facial Cleanser",
                description = "Gentle facial cleanser for all skin types",
                price = 19.99,
                stock = 200,
                soldCount = 298, // Add sold count
                imageUrl = "https://example.com/images/facial-cleanser.jpg",
                sku = "BEAUTY-FC-001",
                weight = 0.3,
                dimensions = "10x5x15 cm",
                featured = false,
            )
        )
        products.add(
            Product(
                name = "Luxury Perfume",
                description = "Premium fragrance for special occasions",
                price = 79.99,
                stock = 50,
                soldCount = 76, // Add sold count
                imageUrl = "https://example.com/images/perfume.jpg",
                sku = "BEAUTY-LP-002",
                weight = 0.2,
                dimensions = "8x8x15 cm",
                featured = true,
            )
        )
    }

    productRepository.saveAll(products)
}