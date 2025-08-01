package com.lcaohoanq.sp.domains.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class UserPreference(
    @Column(nullable = false)
    val preferredLanguage: String? = "en",
    @Column(nullable = false)
    val preferredCurrency: String? = "USD",
)