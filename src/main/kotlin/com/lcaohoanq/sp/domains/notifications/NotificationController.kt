package com.lcaohoanq.sp.domains.notifications

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.domains.notifications.NotificationPort.NotificationRes
import com.lcaohoanq.sp.entities.NotificationEntity
import com.lcaohoanq.sp.enums.NotificationEnum.NotificationType
import com.lcaohoanq.sp.utils.createPageRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.prefix}/notifications")
@Tag(name = "notifications", description = "Notification API")
class NotificationController(
    private val service: NotificationService
) : BaseController() {


    @GetMapping("/paged")
    fun getAllPageable(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "id,asc") sort: String
    ): ResponseEntity<MyApiResponse<Page<NotificationEntity>>> {
        return ok(data = service.getAllNotifications(createPageRequest(page, size, sort)))
    }

    @GetMapping("/{email}")
    @Operation(
        summary = "Get notifications by user ID",
        description = "Fetches all notifications for a specific user based on their user ID."
    )
    fun getByUserId(
        @PathVariable @Schema(defaultValue = "hoangclw@gmail.com") email: String
    ): ResponseEntity<*> {
        val notifications: MutableList<NotificationRes> = service.getNotificationByEmail(
            email
        )
        return ResponseEntity.ok<MutableList<NotificationRes>>(notifications)
    }


    @PostMapping("")
    @Operation(
        summary = "[DEBUG] Add a new notification",
        description = "Creates a new notification and saves it to the database."
    )
    fun create(
        @RequestBody notificationEntity: @Valid NotificationEntity
    ): ResponseEntity<*> {
        service.addNotification(notificationEntity)
        return ResponseEntity.ok<String>("Notification created successfully")
    }

    @PostMapping("/{id}")
    @Operation(
        summary = "[DEBUG] Add a new notification",
        description = "Creates a new notification and saves it to the database."
    )
    fun create(
        @RequestBody notificationEntity: @Valid NotificationEntity,
        @PathVariable id: Long
    ): ResponseEntity<*> {
        service.addNotificationForUser(id, notificationEntity)
        return ResponseEntity.ok<String>("Notification created successfully")
    }

    @PatchMapping("/{id}/read")
    fun markAsRead(@PathVariable id: Long): ResponseEntity<*> {
        service.markAsRead(id)
        return ResponseEntity.ok().build<Any>()
    }

    @PostMapping("/send")
    @Deprecated("")
    fun send(): ResponseEntity<*> {
        val token =
            "fHbU6NCiTkWw-JqPNWI5Y7:APA91bH4zrWYQrm0xXzputlOCx-2OI8DoA8EntQ9haXGu8aIpBKDpfyoMPRRR7hmjuzU8lFHhDoz_P68KwqgChvD3Hwwyn3A2lde18MnUBgn28SBqTVrGVU" // 👈 Replace this with your real device token
        service.sendNotification(token)
        return ResponseEntity.ok<String>("Sent!")
    }

    @PostMapping("/send-to-user")
    @Operation(
        summary = "Send notification to all user devices",
        description = "Send push notification to all registered devices of a user and save to database"
    )
    fun sendToUser(
        @RequestParam @Schema(defaultValue = "hoangclw@gmail.com") email: String,
        @RequestParam @Schema(defaultValue = "Test Notification") title: String,
        @RequestParam @Schema(defaultValue = "This is a test notification sent to all your devices") body: String,
        @RequestParam(defaultValue = "INFO") type: NotificationType
    ): ResponseEntity<*> {
        service.sendNotificationToUser(email, title, body, type)
        return ResponseEntity.ok<String>("Notification sent to all devices for user: $email")
    }
}
