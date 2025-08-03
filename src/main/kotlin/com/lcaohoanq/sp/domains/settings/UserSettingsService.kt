package com.lcaohoanq.sp.domains.settings

import com.lcaohoanq.sp.domains.user.UserPort
import com.lcaohoanq.sp.dto.UpdateUserSettingsDto
import com.lcaohoanq.sp.extension.applyUpdates
import com.lcaohoanq.sp.extension.toUserSettingsResponse
import com.lcaohoanq.sp.repositories.UserSettingsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserSettingsService(
    private val userSettingsRepository: UserSettingsRepository
) : IUserSettingsService {

    /**
     * Get user settings by user ID
     */
    override fun getUserSettingsByUserId(userId: Long): UserPort.UserSettingsResponse {
        return userSettingsRepository.findByUserId(userId).toUserSettingsResponse()
    }

    /**
     * Update user settings
     */
    @Transactional
    override fun updateUserSettings(userId: Long, updates: UpdateUserSettingsDto): UserPort.UserSettingsResponse {
        val settings = userSettingsRepository.findByUserId(userId)
        settings.applyUpdates(updates)
        return userSettingsRepository.save(settings).toUserSettingsResponse()
    }
}
