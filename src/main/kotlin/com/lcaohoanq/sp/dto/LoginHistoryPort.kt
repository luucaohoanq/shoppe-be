package com.lcaohoanq.sp.dto

import com.fasterxml.jackson.annotation.JsonFormat

import java.time.LocalDateTime

interface LoginHistoryPort {

    data class LoginHistoryResponse(
        val id: Long,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        val loginAt: LocalDateTime,
        val ipAddress: String,
        val userAgent: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        val createdAt: LocalDateTime?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
        val updatedAt: LocalDateTime?
    )

}
