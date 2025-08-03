package com.lcaohoanq.sp.domains.notifications

import com.fasterxml.jackson.annotation.JsonInclude
import com.lcaohoanq.sp.entities.NotificationEntity
import com.lcaohoanq.sp.enums.NotificationEnum.*
import java.sql.Timestamp

interface NotificationPort {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class NotificationRes(
        val id: Long?,
        val type: NotificationType?,
        val title: String?,
        val description: String?,
        val time: Timestamp?,
        val read: Boolean?,
        val iconName: NotificationIcon?,
        val iconColorHex: NotificationColor?,
        val email: String?
    ) {
        companion object {
            fun from(notificationEntity: NotificationEntity): NotificationRes {
                return NotificationRes(
                    notificationEntity.id,
                    notificationEntity.type,
                    notificationEntity.title,
                    notificationEntity.description,
                    notificationEntity.createdAt,
                    notificationEntity.read,
                    notificationEntity.iconName,
                    notificationEntity.iconColorHex,
                    notificationEntity.user?.email
                )
            }

            fun fromWithoutEmail(notificationEntity: NotificationEntity): NotificationRes {
                return NotificationRes(
                    notificationEntity.id,
                    notificationEntity.type,
                    notificationEntity.title,
                    notificationEntity.description,
                    notificationEntity.createdAt,
                    notificationEntity.read,
                    notificationEntity.iconName,
                    notificationEntity.iconColorHex,
                    null // explicitly null for email
                )
            }
        }
    }
}
