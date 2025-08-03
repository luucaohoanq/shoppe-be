package com.lcaohoanq.sp.domains.settings

import com.lcaohoanq.sp.domains.user.UserPort
import com.lcaohoanq.sp.dto.UpdateUserSettingsDto

interface IUserSettingsService {
    /**
     * Get user settings by user ID
     */
    fun getUserSettingsByUserId(userId: Long): UserPort.UserSettingsResponse
    
    /**
     * Update user settings
     */
    fun updateUserSettings(userId: Long, updates: UpdateUserSettingsDto): UserPort.UserSettingsResponse
}
