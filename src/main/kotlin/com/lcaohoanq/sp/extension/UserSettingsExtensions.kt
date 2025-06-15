package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.settings.UserSettings
import com.lcaohoanq.sp.domains.user.UserPort

fun UserSettings.toUserSettingsResponse() = UserPort.UserSettingsResponse(
    id = this.id!!,
    twoFaEnabled = this.twoFaEnabled,
    preferredLanguage = this.preferredLanguage,
    darkMode = this.darkMode,
    notificationSettings = this.notificationSettings,
    loginAlerts = this.loginAlerts,
    requestDisableAccount = this.requestDisableAccount,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
