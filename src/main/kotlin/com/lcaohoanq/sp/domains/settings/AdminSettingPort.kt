package com.lcaohoanq.sp.domains.settings

import com.fasterxml.jackson.annotation.JsonFormat
import java.sql.Timestamp

object AdminSettingPort {
    
    data class AdminSettingResponse(
        val id: Int,
        val settingKey: String,
        val settingValue: String,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Ho_Chi_Minh")
        val createdAt: Timestamp?,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Ho_Chi_Minh")
        val updatedAt: Timestamp?
    )
    
    data class AdminSettingUpdateRequest(val value: String)
}