package com.lcaohoanq.sp.domains.token

interface ITokenCleanupService {
    fun cleanupExpiredTokens()
}
