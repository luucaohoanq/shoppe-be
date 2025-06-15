package com.lcaohoanq.sp.domains.token

import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.dto.TokenPort

interface ITokenService {

    fun getAll(): List<TokenPort.TokenResponse>
    fun addToken(userId: Long, token: String): Token
    fun refreshToken(refreshToken: String, user: User): Token
    fun deleteToken(token: String, user: User)
    fun findUserByToken(token: String): Token

}
