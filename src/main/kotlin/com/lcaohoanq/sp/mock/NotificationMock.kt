package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.entities.NotificationEntity
import com.lcaohoanq.sp.entities.UserDeviceToken
import com.lcaohoanq.sp.enums.NotificationEnum
import com.lcaohoanq.sp.repositories.NotificationRepository
import com.lcaohoanq.sp.repositories.UserDeviceTokenRepository

data class NotificationTemplate(
    val title: String,
    val description: (User) -> String,  // để bạn có thể inject user.name động
    val type: NotificationEnum.NotificationType,
    val iconName: NotificationEnum.NotificationIcon,
    val iconColorHex: NotificationEnum.NotificationColor
)

fun initNotifications(users: List<User>, notificationRepository: NotificationRepository) {
    try{
        val templates = listOf(
            NotificationTemplate(
                title = "Welcome",
                description = { user -> "Welcome to our service, ${user.name}!" },
                type = NotificationEnum.NotificationType.INFO,
                iconName = NotificationEnum.NotificationIcon.MESSAGE,
                iconColorHex = NotificationEnum.NotificationColor.BLUE
            ),
            NotificationTemplate(
                title = "Shipping Update",
                description = { user -> "Your order has been shipped, ${user.name}." },
                type = NotificationEnum.NotificationType.SUCCESS,
                iconName = NotificationEnum.NotificationIcon.MESSAGE,
                iconColorHex = NotificationEnum.NotificationColor.GREEN
            ),
            NotificationTemplate(
                title = "Security Alert",
                description = { user -> "Your password has been changed successfully, ${user.name}." },
                type = NotificationEnum.NotificationType.WARNING,
                iconName = NotificationEnum.NotificationIcon.ALERT,
                iconColorHex = NotificationEnum.NotificationColor.YELLOW
            ),
            NotificationTemplate(
                title = "New Message",
                description = { user -> "You have a new message, ${user.name}." },
                type = NotificationEnum.NotificationType.INFO,
                iconName = NotificationEnum.NotificationIcon.MESSAGE,
                iconColorHex = NotificationEnum.NotificationColor.BLUE
            )
        )

        val notifications = users.flatMap { user ->
            templates.map { template ->
                NotificationEntity(
                    user = user,
                    title = template.title,
                    description = template.description(user),
                    type = template.type,
                    iconName = template.iconName,
                    iconColorHex = template.iconColorHex,
                    read = false
                )
            }
        }

        notificationRepository.saveAll(notifications)
    }catch (e: Exception){
        throw RuntimeException("Failed to initialize notifications: ${e.message}", e)
    }
}

fun initUserDeviceTokens(
    users: List<User>,
    userDeviceTokenRepository: UserDeviceTokenRepository
) {
    try{
        val deviceTokens = users.mapIndexed { index, user ->
            UserDeviceToken(
                user = user,
                deviceId = "device-${user.id ?: index}", // fallback nếu user chưa có id
                fcmToken = "fcm_token_${user.id ?: index}",
                deviceName = "Device of ${user.name}",
                platform = if (index % 2 == 0) "Android" else "iOS"
            )
        }

        userDeviceTokenRepository.saveAll(deviceTokens)
    }catch (e: Exception){
        throw RuntimeException("Failed to initialize user device tokens: ${e.message}", e)
    }
}
