package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.domains.categories.Category
import com.lcaohoanq.sp.repositories.CategoryRepository

fun initCategories(categoryRepository: CategoryRepository): List<Category> {
    val categories = listOf(
        Category(
            name = "Electronics",
            description = "Electronic devices and accessories",
            slug = "electronics",
            imageUrl = "https://example.com/images/electronics.jpg",
            active = true
        ),
        Category(
            name = "Clothing",
            description = "Apparel and fashion items",
            slug = "clothing",
            imageUrl = "https://example.com/images/clothing.jpg",
            active = true
        ),
        Category(
            name = "Home & Kitchen",
            description = "Home appliances and kitchen essentials",
            slug = "home-kitchen",
            imageUrl = "https://example.com/images/home-kitchen.jpg",
            active = true
        ),
        Category(
            name = "Books",
            description = "Books, e-books, and publications",
            slug = "books",
            imageUrl = "https://example.com/images/books.jpg",
            active = true
        ),
        Category(
            name = "Beauty & Personal Care",
            description = "Beauty products and personal care items",
            slug = "beauty-personal-care",
            imageUrl = "https://example.com/images/beauty.jpg",
            active = true
        )
    )

    return categoryRepository.saveAll(categories)
}