package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.discount.Voucher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface VoucherRepository: JpaRepository<Voucher, Long> {
    
    @Query("""
        SELECT DISTINCT v FROM Voucher v 
        LEFT JOIN v.productVouchers pv 
        WHERE v.active = true 
        AND v.validFrom <= :currentTime 
        AND v.validUntil >= :currentTime 
        AND v.usedCount < v.usageLimit 
        AND (
            v.condition.requiredProductId IS NULL 
            OR v.condition.requiredProductId = :productId
            OR pv.product.id = :productId
        )
        AND (
            v.condition.requiredCategoryId IS NULL 
            OR v.condition.requiredCategoryId = :categoryId
        )
        ORDER BY v.discountValue DESC
    """)
    fun findAvailableVouchersForProduct(
        @Param("productId") productId: Long?,
        @Param("categoryId") categoryId: Long?,
        @Param("currentTime") currentTime: LocalDateTime
    ): List<Voucher>
    
    @Query("""
        SELECT v FROM Voucher v 
        WHERE v.active = true 
        AND v.validFrom <= :currentTime 
        AND v.validUntil >= :currentTime 
        AND v.usedCount < v.usageLimit
        ORDER BY v.discountValue DESC
    """)
    fun findAllActiveVouchers(@Param("currentTime") currentTime: LocalDateTime): List<Voucher>
}