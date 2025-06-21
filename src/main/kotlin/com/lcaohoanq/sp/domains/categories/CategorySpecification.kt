package com.lcaohoanq.sp.domains.categories

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

class CategorySpecification(private val search: String?) : Specification<Category> {
    override fun toPredicate(
        root: Root<Category>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        // If search is null or blank, return a conjunction (match all)
        if (search.isNullOrBlank()) {
            return criteriaBuilder.conjunction()
        }

        val searchLike = "%$search%"
        return criteriaBuilder.or(
            criteriaBuilder.like(root.get<String>("name"), searchLike),
            criteriaBuilder.like(root.get<String>("description"), searchLike),
            criteriaBuilder.like(root.get<String>("parentId"), searchLike)
        )
    }
}