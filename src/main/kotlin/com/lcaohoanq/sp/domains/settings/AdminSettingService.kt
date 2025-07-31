package com.lcaohoanq.sp.domains.settings

import com.lcaohoanq.sp.entities.AdminSetting
import com.lcaohoanq.sp.repositories.AdminSettingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface IAdminSettingService {
    fun getAllSettings(): List<AdminSetting>
    fun getSettingByKey(key: String): AdminSetting?
    fun createSetting(setting: AdminSetting): AdminSetting
    fun updateSetting(key: String, value: String): AdminSetting?
    fun deleteSetting(key: String): Boolean
    fun getSettingValueByKey(key: String, defaultValue: String): String
}

@Service
class AdminSettingService(
    private val adminSettingRepository: AdminSettingRepository
) : IAdminSettingService {

    override fun getAllSettings(): List<AdminSetting> {
        return adminSettingRepository.findAll()
    }

    override fun getSettingByKey(key: String): AdminSetting? {
        return adminSettingRepository.findBySettingKey(key)
    }

    @Transactional
    override fun createSetting(setting: AdminSetting): AdminSetting {
        return adminSettingRepository.save(setting)
    }

    @Transactional
    override fun updateSetting(key: String, value: String): AdminSetting? {
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