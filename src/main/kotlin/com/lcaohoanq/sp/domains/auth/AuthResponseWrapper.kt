package com.lcaohoanq.sp.domains.auth

import com.lcaohoanq.sp.dto.AuthPort

data class AuthResponseWrapper(
    val message: String?,
    val data: AuthPort.AuthResponse?,
    val statusCode: Int?,
    val isSuccess: Boolean?,
    val reason: String? = null
)
