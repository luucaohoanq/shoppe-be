package com.lcaohoanq.sp.domains.settings

import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonFormat

object AdminSettingPort {
    
    data class AdminSettingResponse(
        val id: Int,
        val settingKey: String,
        val settingValue: String,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Ho_Chi_Minh")
        val createdAt: LocalDateTime?,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Ho_Chi_Minh")
        val updatedAt: LocalDateTime?
    )
    
    data class AdminSettingUpdateRequest(val value: String)
}