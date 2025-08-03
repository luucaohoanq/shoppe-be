package com.lcaohoanq.sp.domains.notifications

interface FcmTokenPort {

    data class UserDeviceTokenDto(
        val id: Long?,
        val userId: Long?,
        val deviceId: String?,
        val fcmToken: String?,
        val deviceName: String?,
        val platform: String?
    )

    data class CreateUserDeviceTokenReq(
        val userId: Long,
        val deviceId: String,
        val fcmToken: String,
        val deviceName: String?,
        val platform: String?
    )

    data class UpdateUserDeviceTokenReq(
        val fcmToken: String?,
        val deviceName: String?,
        val platform: String?
    )
}
