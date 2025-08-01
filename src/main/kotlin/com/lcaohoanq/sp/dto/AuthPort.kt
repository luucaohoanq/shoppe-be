package com.lcaohoanq.sp.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.lcaohoanq.sp.enums.UserEnum
import io.swagger.v3.oas.annotations.media.Schema

interface AuthPort {

    data class AuthRequest(
        @Schema(defaultValue = "ad@gmail.com")
        val email: String,
        @Schema(defaultValue = "1")
        val password: String
    )

    data class AuthResponse(
        @JsonProperty("token") val token: TokenPort.TokenResponse
    )

    data class SignUpReq(
        val name: String,
        val email: String,
        val address: String,
        val phoneNumber: String,
        val gender: UserEnum.Gender? = UserEnum.Gender.FEMALE,
        val password: String,
        val status: UserEnum.Status? = UserEnum.Status.UNVERIFIED,
    )

    data class VerifyEmailReq(
        val email: String,
    )

    data class Verify2FAReq(
        val secret: String,
        val code: String,
    )

    data class ChangePasswordReq(
        val email: String,
        val password: String,

       @JsonProperty("new_password") val newPassword: String
    )

}
