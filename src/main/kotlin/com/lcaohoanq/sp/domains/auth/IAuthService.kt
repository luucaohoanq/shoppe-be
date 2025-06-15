package com.lcaohoanq.sp.domains.auth

import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.domains.user.UserPort
import com.lcaohoanq.sp.dto.AuthPort
import com.lcaohoanq.sp.dto.TokenPort


interface IAuthService {

    fun login(account: AuthPort.AuthRequest): LoginResult
    fun register(newAccount: AuthPort.SignUpReq)
    fun getUserDetailsFromToken(token: String): UserPort.UserResponse
    fun getCurrentAuthenticatedUser(): User
    fun refreshToken(refreshTokenDTO: TokenPort.RefreshTokenDTO): AuthPort.AuthResponse
    fun logout(token: String, user: User)
    fun generateTokenFromEmail(email: String): String
    fun changePassword(req: AuthPort.ChangePasswordReq)
    fun verifyAccount(token: String): Unit

}
