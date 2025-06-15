package com.lcaohoanq.sp.domains.otp

interface OtpPort {

    data class OtpReq(
        val email: String,
        val otp: String
    )

}
