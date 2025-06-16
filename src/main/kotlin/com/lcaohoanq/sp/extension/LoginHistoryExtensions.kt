package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.loginhistory.LoginHistory
import com.lcaohoanq.sp.dto.LoginHistoryPort

fun LoginHistory.toLoginHistoryResponse(): LoginHistoryPort.LoginHistoryResponse {
    return LoginHistoryPort.LoginHistoryResponse(
        id = this.id!!,
        loginAt = this.loginAt,
        ipAddress = this.ipAddress!!,
        userAgent = this.userAgent!!,
        createdAt = this.createdAt,
        updatedAt = this.lastModifiedOn
    )
}
