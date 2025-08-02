package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.entities.SystemSetting
import com.lcaohoanq.sp.repositories.AdminSettingRepository

fun initAdminSettings(adminSettingRepository: AdminSettingRepository): List<SystemSetting> {
    val systemSettings = listOf(
        SystemSetting().apply {
            settingKey = "site.name"
            settingValue = "Shoppe"
            description = "Name of the e-commerce site"
        },
        SystemSetting().apply {
            settingKey = "site.description"
            settingValue = "An e-commerce platform"
            description = "Description of the e-commerce site"
        },
        SystemSetting().apply {
            settingKey = "site.contact.email"
            settingValue = "contact@shoppe.com"
            description = "Contact email for the site"
        },
        SystemSetting().apply {
            settingKey = "site.contact.phone"
            settingValue = "+84123456789"
            description = "Contact phone number for the site"
        },
        SystemSetting().apply {
            settingKey = "payment.currency"
            settingValue = "USD"
            description = "Default currency for payments"
        },
        SystemSetting().apply {
            settingKey = "payment.methods"
            settingValue = "Credit Card,PayPal,Bank Transfer"
            description = "Available payment methods"
        },
        SystemSetting().apply {
            settingKey = "order.auto_confirm"
            settingValue = "false"
            description = "Automatically confirm orders after payment"
        },
        SystemSetting().apply {
            settingKey = "user.registration.enabled"
            settingValue = "true"
            description = "Enable user registration"
        },
        SystemSetting().apply {
            settingKey = "user.verification.required"
            settingValue = "true"
            description = "Require email verification for new users"
        },
        SystemSetting().apply {
            settingKey = "maintenance.mode"
            settingValue = "false"
            description = "Enable maintenance mode for the site"
        },
        SystemSetting().apply {
            settingKey = "file.max_upload_size"
            settingValue = "10485760" // 10 MB in bytes
            description = "Maximum file upload size in bytes"
        }
    )

    return adminSettingRepository.saveAll(systemSettings)
}