package com.lcaohoanq.sp.domains.otp

import BaseEntity
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "otps")
data class Otp(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "email")
    val email: String,

    @Column(name = "otp")
    val otp: String,

    @JsonProperty("expired_at")
    @Column(name = "expired_at", nullable = false)
    val expiredAt: java.time.LocalDateTime, // in milliseconds

    @JsonProperty("is_used")
    @Column(name = "is_used")
    var isUsed: Boolean = false,

    @JsonProperty("is_expired")
    @Column(name = "is_expired")
    var isExpired: Boolean = false
) : BaseEntity()
