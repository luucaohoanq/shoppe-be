package com.lcaohoanq.sp.domains.headquarters

import com.fasterxml.jackson.annotation.JsonProperty
import com.lcaohoanq.sp.bases.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "headquarters")
class Headquarters(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    @Column(name = "region", nullable = false, unique = true)
    val region: Int,

    @Column(name = "domain_url", nullable = false)
    @JsonProperty("domain_url")
    val domainUrl: String
) : BaseEntity()
