package com.lcaohoanq.sp.domains.notifications

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import com.lcaohoanq.sp.domains.notifications.NotificationPort.NotificationRes
import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.entities.NotificationEntity
import com.lcaohoanq.sp.enums.NotificationEnum.*
import com.lcaohoanq.sp.repositories.NotificationRepository
import com.lcaohoanq.sp.repositories.UserRepository
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class NotificationServiceImpl(
    private val repository: NotificationRepository,
    private val fcmTokenService: FcmTokenService,
    private val userRepository: UserRepository,
) : NotificationService {

    private val log = KotlinLogging.logger { }

    override fun getAllNotifications(pageable: Pageable): Page<NotificationEntity> {
        return repository.findAll(pageable)
    }

    override fun getNotificationByEmail(email: String): MutableList<NotificationRes> {
        return repository.findByUserEmailOrderByCreatedAtDesc(email).stream()
            .map { notificationEntity: NotificationEntity ->
                NotificationRes.fromWithoutEmail(
                    notificationEntity
                )
            }
            .toList()
    }

    override fun addNotificationForUser(userId: Long, notificationEntity: NotificationEntity) {
        val user: User = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User does not exist.") }

        val newNotification = NotificationEntity(
            title = notificationEntity.title,
            type = notificationEntity.type,
            description = notificationEntity.description,
            user = user,
            iconName = notificationEntity.iconName,
            iconColorHex = notificationEntity.iconColorHex,
        )

        repository.save(newNotification)
    }

    override fun addNotification(notificationEntity: NotificationEntity) {
        val newNotification = NotificationEntity(
            title = notificationEntity.title,
            type = notificationEntity.type,
            description = notificationEntity.description,
            iconName = notificationEntity.iconName,
            iconColorHex = notificationEntity.iconColorHex,
        )

        repository.save(newNotification)
    }

    override fun markAsRead(id: Long) {
        val notification: NotificationEntity = repository.findById(id).orElseThrow()
        if (!notification.read) {
            notification.read = true
            repository.save(notification)
        }
    }

    override fun sendNotification(token: String) {
        val notification = Notification.builder()
            .setTitle("Hello")
            .setBody("This is a test notification")
            .build()

        val message = Message.builder()
            .setToken(token)
            .setNotification(notification)
            .build()

        val response = FirebaseMessaging.getInstance().send(message)
        log.info("Sent notification! Response: {}", response)
    }

    override fun sendNotificationToUser(
        email: String, title: String, body: String,
        type: NotificationType
    ) {
        // Get all FCM tokens for this user
        val fcmTokens: MutableList<String> = fcmTokenService.getFcmTokensByEmail(email)

        if (fcmTokens.isEmpty()) {
            log.warn("No FCM tokens found for user: {}", email)
            return
        }

        // Create Firebase notification
        val notification = Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build()

        if (fcmTokens.size == 1) {
            // Send to single device
            val message = Message.builder()
                .setToken(fcmTokens[0])
                .setNotification(notification)
                .build()

            val response = FirebaseMessaging.getInstance().send(message)
            log.info(
                "Sent notification to user {} (single device). Response: {}",
                email,
                response
            )
        } else {
            // Send to multiple devices
            val message = MulticastMessage.builder()
                .addAllTokens(fcmTokens)
                .setNotification(notification)
                .build()

            val response = FirebaseMessaging.getInstance().sendEachForMulticast(message)
            log.info(
                "Sent notification to user {} ({} devices). Success: {}, Failure: {}",
                email, fcmTokens.size, response.getSuccessCount(),
                response.getFailureCount()
            )
        }

        // Save notification to database for user to view in app
        saveNotificationForUser(
            email, title, body, type,
            NotificationIcon.MESSAGE,
            NotificationColor.BLUE
        )
    }

    override fun saveNotificationForUser(
        email: String, title: String,
        description: String,
        type: NotificationType,
        icon: NotificationIcon,
        color: NotificationColor
    ): NotificationEntity {
        val user = userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User with email $email does not exist.") }

        val notification = NotificationEntity(
            title = title,
            type = type,
            description = description,
            user = user,
            iconName = icon,
            iconColorHex = color
        )

        return repository.save(notification)
    }
}
