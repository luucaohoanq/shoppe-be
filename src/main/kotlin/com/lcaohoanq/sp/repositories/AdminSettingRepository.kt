package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.entities.SystemSetting
import org.springframework.data.jpa.repository.JpaRepository

interface AdminSettingRepository: JpaRepository<SystemSetting, Int> {
    fun findBySettingKey(settingKey: String): SystemSetting?
}