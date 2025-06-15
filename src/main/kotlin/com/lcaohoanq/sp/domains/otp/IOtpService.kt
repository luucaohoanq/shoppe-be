package com.lcaohoanq.sp.domains.otp

import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.dto.OtpPort

interface IOtpService {
    fun createOtpFor(user: User, otpRequest: OtpPort.OtpReq)
    fun disableOtp(id: Long)
    fun getOtpByEmailOtp(email: String, otp: String): Otp?
    fun setOtpExpired()
    fun generateOtp(): String
    fun getAllOtps(): List<OtpPort.OtpRes>

}
