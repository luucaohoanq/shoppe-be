package com.lcaohoanq.sp.domains.auth

import com.lcaohoanq.sp.dto.TokenPort

sealed interface LoginResult

data class LoginSuccess(
    val token: TokenPort.TokenResponse
) : LoginResult

data class Login2FARequired(
    val tempToken: String, // optional, or null if unused
    val is2FARequired: Boolean = true
) : LoginResult
