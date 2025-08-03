package com.lcaohoanq.sp.domains.notifications

import com.lcaohoanq.sp.domains.notifications.FcmTokenPort.*
import com.lcaohoanq.sp.entities.UserDeviceToken
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface FcmTokenService {
    fun getAll(pageable: Pageable): Page<UserDeviceToken>
    fun getById(id: Long): UserDeviceToken
    fun create(req: CreateUserDeviceTokenReq): UserDeviceToken
    fun update(id: Long, req: UpdateUserDeviceTokenReq): UserDeviceToken
    fun createOrUpdate(req: CreateUserDeviceTokenReq): UserDeviceToken
    fun getFcmTokensByEmail(email: String): MutableList<String>
}
