package com.lcaohoanq.sp.domains.product

import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.domains.categories.Category
import com.lcaohoanq.sp.domains.discount.ProductVoucher
import jakarta.persistence.*
import net.minidev.json.annotate.JsonIgnore

@Entity
@Table(name = "products")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String = "",

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String = "",

    @Column(name = "price", nullable = false)
    var price: Double = 0.0,

    @Column(name = "stock", nullable = false)
    var stock: Int = 0,

    @Column(name = "shop_id", nullable = false)
    var shopId: Long = 0,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @Column(name = "images")
    var images: List<String>? = null,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: ProductStatus = ProductStatus.ACTIVE,

    @Column(name = "rating")
    var rating: Double = 0.0,

    @Column(name = "review_count")
    var reviewCount: Int = 0,

    @Column(name = "sold_count")
    var soldCount: Int = 0,

    @Column(name = "weight")
    var weight: Double? = null,

    @Column(name = "dimensions")
    var dimensions: String? = null,

    @Column(name = "sku", unique = true, nullable = true)
    var sku: String? = null,

    @Column(name = "featured", nullable = true)
    var featured: Boolean? = false,

    // Relationship with Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    var category: Category? = null,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL])
    @JsonIgnore
    var productVouchers: MutableSet<ProductVoucher> = mutableSetOf()

    ) : BaseEntity() {

    enum class ProductStatus {
        ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED
    }

    fun updateStock(quantity: Int) {
        this.stock = maxOf(0, this.stock - quantity)
        if (this.stock == 0) {
            this.status = ProductStatus.OUT_OF_STOCK
        }
    }

    fun addStock(quantity: Int) {
        this.stock += quantity
        if (this.stock > 0 && this.status == ProductStatus.OUT_OF_STOCK) {
            this.status = ProductStatus.ACTIVE
        }
    }

    fun updateRating(newRating: Double) {
        val totalRating = this.rating * this.reviewCount + newRating
        this.reviewCount += 1
        this.rating = totalRating / this.reviewCount
    }

    fun isAvailable(): Boolean {
        return status == ProductStatus.ACTIVE && stock > 0
    }
}
