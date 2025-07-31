package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.entities.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface CouponRepository : JpaRepository<Coupon, Long> {
    
    fun findByCode(code: String): Coupon?
    
    fun findByActiveTrue(): List<Coupon>
    
    @Query("SELECT c FROM Coupon c WHERE c.active = true AND c.expiryDate > :now")
    fun findActiveCoupons(@Param("now") now: LocalDateTime): List<Coupon>
    
    @Query("SELECT c FROM Coupon c WHERE c.code = :code AND c.active = true AND c.expiryDate > :now")
    fun findValidCouponByCode(@Param("code") code: String, @Param("now") now: LocalDateTime): Coupon?
    
    @Query("SELECT c FROM Coupon c WHERE c.discountPercent >= :minDiscount")
    fun findByMinimumDiscount(@Param("minDiscount") minDiscount: Double): List<Coupon>
    
    @Query("SELECT c FROM Coupon c WHERE c.expiryDate BETWEEN :startDate AND :endDate")
    fun findCouponsExpiringBetween(
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Coupon>
}
