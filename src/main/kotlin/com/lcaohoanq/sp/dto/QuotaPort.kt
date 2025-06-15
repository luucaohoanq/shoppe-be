package com.lcaohoanq.sp.dto

import com.lcaohoanq.sp.metadata.QuotaMeta

interface QuotaPort {

    data class QuotaResponse(
        val userId: Long,
        val quotas: List<QuotaMeta>
    )

}
