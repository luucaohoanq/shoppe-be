package com.lcaohoanq.sp.domains.product

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class ProductSpecification(private val searchTerm: String) : Specification<Product> {

    override fun toPredicate(
        root: Root<Product?>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        if (searchTerm.isBlank()) {
            return null
        }

        val searchPattern = "%${searchTerm.lowercase()}%"

        return criteriaBuilder.or(
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                searchPattern
            ),
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get("description")),
                searchPattern
            )
        )
    }

    companion object {
        
        fun withFilters(
            name: String?,
            categoryId: Long?,
            shopId: Long?,
            minPrice: Double?,
            maxPrice: Double?,
            status: Product.ProductStatus?,
            inStock: Boolean?
        ): Specification<Product> {
            return Specification { root, query, criteriaBuilder ->
                val predicates = mutableListOf<Predicate>()

                // Name filter
                name?.let { nameValue ->
                    if (nameValue.isNotBlank()) {
                        predicates.add(
                            criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%${nameValue.lowercase()}%"
                            )
                        )
                    }
                }

                // Category filter
                categoryId?.let { catId ->
                    predicates.add(criteriaBuilder.equal(root.get<Long>("categoryId"), catId))
                }

                // Shop filter
                shopId?.let { shopIdValue ->
                    predicates.add(criteriaBuilder.equal(root.get<Long>("shopId"), shopIdValue))
                }

                // Price range filter
                minPrice?.let { minPriceValue ->
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPriceValue))
                }

                maxPrice?.let { maxPriceValue ->
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPriceValue))
                }

                // Status filter
                status?.let { statusValue ->
                    predicates.add(criteriaBuilder.equal(root.get<Product.ProductStatus>("status"), statusValue))
                }

                // Stock filter
                inStock?.let { inStockValue ->
                    if (inStockValue) {
                        predicates.add(criteriaBuilder.greaterThan(root.get("stock"), 0))
                    } else {
                        predicates.add(criteriaBuilder.equal(root.get<Int>("stock"), 0))
                    }
                }

                criteriaBuilder.and(*predicates.toTypedArray())
            }
        }

        fun byCategory(categoryId: Long): Specification<Product> {
            return Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<Long>("categoryId"), categoryId)
            }
        }

        fun byShop(shopId: Long): Specification<Product> {
            return Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<Long>("shopId"), shopId)
            }
        }

        fun byStatus(status: Product.ProductStatus): Specification<Product> {
            return Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<Product.ProductStatus>("status"), status)
            }
        }

        fun byPriceRange(minPrice: Double?, maxPrice: Double?): Specification<Product> {
            return Specification { root, _, criteriaBuilder ->
                val predicates = mutableListOf<Predicate>()

                minPrice?.let { min ->
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), min))
                }

                maxPrice?.let { max ->
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), max))
                }

                if (predicates.isNotEmpty()) {
                    criteriaBuilder.and(*predicates.toTypedArray())
                } else {
                    null
                }
            }
        }

        fun inStock(): Specification<Product> {
            return Specification { root, _, criteriaBuilder ->
                criteriaBuilder.greaterThan(root.get("stock"), 0)
            }
        }

        fun available(): Specification<Product> {
            return Specification { root, _, criteriaBuilder ->
                criteriaBuilder.and(
                    criteriaBuilder.equal(root.get<Product.ProductStatus>("status"), Product.ProductStatus.ACTIVE),
                    criteriaBuilder.greaterThan(root.get("stock"), 0)
                )
            }
        }

        fun lowStock(threshold: Int): Specification<Product> {
            return Specification { root, _, criteriaBuilder ->
                criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("stock"), threshold),
                    criteriaBuilder.equal(root.get<Product.ProductStatus>("status"), Product.ProductStatus.ACTIVE)
                )
            }
        }
    }
}
