package com.lcaohoanq.sp.dto

import com.lcaohoanq.sp.domains.settings.notifications.NotificationSettings

data class UpdateUserSettingsDto(
    val twoFaEnabled: Boolean? = null,
    val preferredLanguage: String? = null,
    val darkMode: Boolean? = null,
    val notificationSettings: UpdateNotificationSettingsDto? = null,
    val loginAlerts: Boolean? = null,
    val requestDisableAccount: Boolean? = null
)

data class UpdateNotificationSettingsDto(
    val email: UpdateEmailNotificationSettingsDto? = null,
    val sms: UpdateSmsNotificationSettingsDto? = null,
    val zalo: UpdateZaloNotificationSettingsDto? = null
)

data class UpdateEmailNotificationSettingsDto(
    val masterEnabled: Boolean? = null,
    val order: Boolean? = null,
    val promo: Boolean? = null,
    val survey: Boolean? = null
)

data class UpdateSmsNotificationSettingsDto(
    val masterEnabled: Boolean? = null,
    val promo: Boolean? = null
)

data class UpdateZaloNotificationSettingsDto(
    val masterEnabled: Boolean? = null,
    val promo: Boolean? = null
)