package com.lcaohoanq.sp.domains.quota

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "api_quotas")
class ApiQuota(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    val userId: Long,
    val apiEndpoint: String,
    var requestCount: Int = 0,
    val maxRequests: Int,
    var resetTime: LocalDateTime
)
