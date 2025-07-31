package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.settings.AdminSettingPort
import com.lcaohoanq.sp.entities.AdminSetting

fun AdminSetting.toAdminSettingResponse() = AdminSettingPort.AdminSettingResponse(
    id = this.id ?: 0,
    settingKey = this.settingKey ?: "",
    settingValue = this.settingValue ?: "",
    createdAt = this.createdAt,
    updatedAt = this.lastModifiedOn
)