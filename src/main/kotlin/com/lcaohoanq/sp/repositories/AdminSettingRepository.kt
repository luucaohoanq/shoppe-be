package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.entities.AdminSetting
import org.springframework.data.jpa.repository.JpaRepository

interface AdminSettingRepository: JpaRepository<AdminSetting, Int> {
    fun findBySettingKey(settingKey: String): AdminSetting?
}