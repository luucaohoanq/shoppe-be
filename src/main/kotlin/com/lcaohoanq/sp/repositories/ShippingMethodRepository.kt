package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.entities.ShippingMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ShippingMethodRepository : JpaRepository<ShippingMethod, Long> {
    
    fun findByName(name: String): ShippingMethod?
    
    fun findByActiveTrue(): List<ShippingMethod>
    
    @Query("SELECT sm FROM ShippingMethod sm WHERE sm.active = true ORDER BY sm.cost ASC")
    fun findActiveShippingMethodsOrderByCost(): List<ShippingMethod>
    
    @Query("SELECT sm FROM ShippingMethod sm WHERE sm.cost BETWEEN :minCost AND :maxCost")
    fun findByCostBetween(@Param("minCost") minCost: Double, @Param("maxCost") maxCost: Double): List<ShippingMethod>
    
    @Query("SELECT sm FROM ShippingMethod sm WHERE sm.estimatedDays <= :maxDays AND sm.active = true")
    fun findByMaxDeliveryDays(@Param("maxDays") maxDays: Int): List<ShippingMethod>
}
