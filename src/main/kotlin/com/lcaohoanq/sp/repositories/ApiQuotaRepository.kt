package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.quota.ApiQuota
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ApiQuotaRepository : JpaRepository<ApiQuota, Long> {
    fun findByUserIdAndApiEndpoint(userId: Long, apiEndpoint: String): Optional<ApiQuota>
    fun findByUserId(userId: Long): List<ApiQuota>?
}
