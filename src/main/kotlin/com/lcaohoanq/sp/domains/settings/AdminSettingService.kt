package com.lcaohoanq.sp.domains.settings

import com.lcaohoanq.sp.entities.SystemSetting
import com.lcaohoanq.sp.repositories.AdminSettingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface IAdminSettingService {
    fun getAllSettings(): List<SystemSetting>
    fun getSettingByKey(key: String): SystemSetting?
    fun createSetting(setting: SystemSetting): SystemSetting
    fun updateSetting(key: String, value: String): SystemSetting?
    fun deleteSetting(key: String): Boolean
    fun getSettingValueByKey(key: String, defaultValue: String): String
}

@Service
class AdminSettingService(
    private val adminSettingRepository: AdminSettingRepository
) : IAdminSettingService {

    override fun getAllSettings(): List<SystemSetting> {
        return adminSettingRepository.findAll()
    }

    override fun getSettingByKey(key: String): SystemSetting? {
        return adminSettingRepository.findBySettingKey(key)
    }

    @Transactional
    override fun createSetting(setting: SystemSetting): SystemSetting {
        return adminSettingRepository.save(setting)
    }

    @Transactional
    override fun updateSetting(key: String, value: String): SystemSetting? {
        val setting = adminSettingRepository.findBySettingKey(key) ?: return null
        setting.settingValue = value
        return adminSettingRepository.save(setting)
    }

    @Transactional
    override fun deleteSetting(key: String): Boolean {
        val setting = adminSettingRepository.findBySettingKey(key) ?: return false
        adminSettingRepository.delete(setting)
        return true
    }

    override fun getSettingValueByKey(key: String, defaultValue: String): String {
        return adminSettingRepository.findBySettingKey(key)?.settingValue ?: defaultValue
    }
}