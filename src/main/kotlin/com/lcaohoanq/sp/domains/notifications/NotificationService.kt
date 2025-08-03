package com.lcaohoanq.sp.domains.notifications

import com.lcaohoanq.sp.domains.notifications.NotificationPort.NotificationRes
import com.lcaohoanq.sp.entities.NotificationEntity
import com.lcaohoanq.sp.enums.NotificationEnum.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NotificationService {
    fun getAllNotifications(pageable: Pageable): Page<NotificationEntity>
    fun getNotificationByEmail(email: String): MutableList<NotificationRes>
    fun addNotification(notificationEntity: NotificationEntity)
    fun addNotificationForUser(userId: Long, notificationEntity: NotificationEntity)
    fun markAsRead(id: Long)

    fun sendNotification(token: String)

    fun sendNotificationToUser(
        email: String, title: String, body: String,
        type: NotificationType
    )

    fun saveNotificationForUser(
        email: String, title: String, description: String,
        type: NotificationType,
        icon: NotificationIcon,
        color: NotificationColor
    ): NotificationEntity
}
