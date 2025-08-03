package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.entities.UserDeviceToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserDeviceTokenRepository : JpaRepository<UserDeviceToken, Long> {
    fun findByDeviceId(deviceId: String): Optional<UserDeviceToken>

    fun findAllByUserEmail(email: String): List<UserDeviceToken>

    fun findByUserIdAndDeviceId(
        userId: Long,
        deviceId: String
    ): Optional<UserDeviceToken>
}
