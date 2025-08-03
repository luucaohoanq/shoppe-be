package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.settings.UserSettings
import com.lcaohoanq.sp.domains.settings.notifications.EmailNotificationSettings
import com.lcaohoanq.sp.domains.settings.notifications.NotificationSettings
import com.lcaohoanq.sp.domains.settings.notifications.SmsNotificationSettings
import com.lcaohoanq.sp.domains.settings.notifications.ZaloNotificationSettings
import com.lcaohoanq.sp.dto.UpdateEmailNotificationSettingsDto
import com.lcaohoanq.sp.dto.UpdateNotificationSettingsDto
import com.lcaohoanq.sp.dto.UpdateSmsNotificationSettingsDto
import com.lcaohoanq.sp.dto.UpdateUserSettingsDto
import com.lcaohoanq.sp.dto.UpdateZaloNotificationSettingsDto

/**
 * Apply updates from UpdateUserSettingsDto to UserSettings entity
 */
fun UserSettings.applyUpdates(updates: UpdateUserSettingsDto): UserSettings {
    updates.twoFaEnabled?.let { this.twoFaEnabled = it }
    updates.darkMode?.let { this.darkMode = it }
    updates.loginAlerts?.let { this.loginAlerts = it }
    updates.requestDisableAccount?.let { this.requestDisableAccount = it }
    updates.notificationSettings?.let { this.applyNotificationSettingsUpdates(it) }
    return this
}

/**
 * Apply updates from UpdateNotificationSettingsDto to NotificationSettings
 */
private fun UserSettings.applyNotificationSettingsUpdates(updates: UpdateNotificationSettingsDto) {
    updates.email?.let { applyEmailNotificationSettingsUpdates(it) }
    updates.sms?.let { applySmsNotificationSettingsUpdates(it) }
    updates.zalo?.let { applyZaloNotificationSettingsUpdates(it) }
}

/**
 * Apply updates from UpdateEmailNotificationSettingsDto to EmailNotificationSettings
 */
private fun UserSettings.applyEmailNotificationSettingsUpdates(updates: UpdateEmailNotificationSettingsDto) {
    updates.masterEnabled?.let { this.notificationSettings.email.masterEnabled = it }
    updates.order?.let { this.notificationSettings.email.order = it }
    updates.promo?.let { this.notificationSettings.email.promo = it }
    updates.survey?.let { this.notificationSettings.email.survey = it }
}

/**
 * Apply updates from UpdateSmsNotificationSettingsDto to SmsNotificationSettings
 */
private fun UserSettings.applySmsNotificationSettingsUpdates(updates: UpdateSmsNotificationSettingsDto) {
    updates.masterEnabled?.let { this.notificationSettings.sms.masterEnabled = it }
    updates.promo?.let { this.notificationSettings.sms.promo = it }
}

/**
 * Apply updates from UpdateZaloNotificationSettingsDto to ZaloNotificationSettings
 */
private fun UserSettings.applyZaloNotificationSettingsUpdates(updates: UpdateZaloNotificationSettingsDto) {
    updates.masterEnabled?.let { this.notificationSettings.zalo.masterEnabled = it }
    updates.promo?.let { this.notificationSettings.zalo.promo = it }
}