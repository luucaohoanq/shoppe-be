package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.otp.Otp
import com.lcaohoanq.sp.dto.OtpPort

fun Otp.toOtpResponse(): OtpPort.OtpRes {
    return OtpPort.OtpRes(
        id = this.id!!,
        email = this.email,
        otp = this.otp,
        expiredAt = this.expiredAt,
        isUsed = this.isUsed,
        isExpired = this.isExpired
    )
}
