package com.lcaohoanq.sp.domains.notifications;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcaohoanq.sp.domains.notifications.NotificationEntity.NotificationColor;
import com.lcaohoanq.sp.domains.notifications.NotificationEntity.NotificationIcon;
import com.lcaohoanq.sp.domains.notifications.NotificationEntity.NotificationType;
import java.time.LocalDateTime;

public interface NotificationPort {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record NotificationRes(
        Long id,
        NotificationType type,
        String title,
        String description,
        LocalDateTime time,
        @JsonProperty("isRead") Boolean read,
        NotificationIcon iconName,
        NotificationColor iconColorHex,
        String email
    ){
        public static NotificationRes from(NotificationEntity notificationEntity) {
            return new NotificationRes(
                notificationEntity.getId(),
                notificationEntity.getType(),
                notificationEntity.getTitle(),
                notificationEntity.getDescription(),
                notificationEntity.getTime(),
                notificationEntity.isRead(),
                notificationEntity.getIconName(),
                notificationEntity.getIconColorHex(),
                notificationEntity.getEmail()
            );
        }

        public static NotificationRes fromWithoutEmail(NotificationEntity notificationEntity) {
            return new NotificationRes(
                notificationEntity.getId(),
                notificationEntity.getType(),
                notificationEntity.getTitle(),
                notificationEntity.getDescription(),
                notificationEntity.getTime(),
                notificationEntity.isRead(),
                notificationEntity.getIconName(),
                notificationEntity.getIconColorHex(),
                null // explicitly null for email
            );
        }

    }

}
