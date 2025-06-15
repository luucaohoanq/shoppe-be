package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.settings.UserSettings
import org.springframework.data.jpa.repository.JpaRepository

interface UserSettingsRepository: JpaRepository<UserSettings, Long> {

    fun findByUserId(userId: Long): UserSettings

}
