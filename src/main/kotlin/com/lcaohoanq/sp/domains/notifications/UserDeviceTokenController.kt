package com.lcaohoanq.sp.domains.notifications

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.domains.notifications.FcmTokenPort.CreateUserDeviceTokenReq
import com.lcaohoanq.sp.domains.notifications.FcmTokenPort.UpdateUserDeviceTokenReq
import com.lcaohoanq.sp.entities.UserDeviceToken
import com.lcaohoanq.sp.utils.createPageRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.prefix}/notifications/user-device-tokens")
@RequiredArgsConstructor
@Tag(name = "user-device-tokens", description = "User Device Token API")
class UserDeviceTokenController(
    private val service: FcmTokenServiceImpl
): BaseController() {


    @GetMapping
    @Operation(
        summary = "Get all user device tokens",
        description = "Fetches all user device tokens with pagination support."
    )
    fun getAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "id,asc") sort: String
    ): ResponseEntity<MyApiResponse<Page<UserDeviceToken>>> {
        return ok(data = service.getAll(createPageRequest(page, size, sort)))
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get user device token by ID",
        description = "Fetches a user device token by its unique ID."
    )
    fun getById(@PathVariable id: Long): UserDeviceToken {
        return service.getById(id)
    }

    @PostMapping
    @Operation(
        summary = "Create or update user device token",
        description = "Creates a new user device token or updates an existing one based on the provided request."
    )
    fun createOrUpdate(@RequestBody req: CreateUserDeviceTokenReq): ResponseEntity<UserDeviceToken> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.createOrUpdate(req))
    }

    @PatchMapping("/{id}")
    @Operation(
        summary = "Update user device token",
        description = "Updates an existing user device token by its unique ID."
    )
    fun update(
        @PathVariable id: Long,
        @RequestBody req: UpdateUserDeviceTokenReq
    ): UserDeviceToken {
        return service.update(id, req)
    }

    @GetMapping("/user/{email}/tokens")
    @Operation(
        summary = "Get FCM tokens by user email",
        description = "Fetches all FCM tokens associated with a specific user email."
    )
    fun getFcmTokensByEmail(@PathVariable email: String): MutableList<String> {
        return service.getFcmTokensByEmail(email)
    }

    @GetMapping("/me")
    @Operation(
        summary = "Get my FCM tokens",
        description = "Fetches all FCM tokens associated with the currently authenticated user."
    )
    fun getMyFcmTokens(
        request: HttpServletRequest
    ): ResponseEntity<MyApiResponse<MutableList<String>>> {
        val token = request.getHeader("Authorization")
        if (token == null || !token.startsWith("Bearer ")) {
            return unauthorized(reason = "Token is empty")
        }
        val email = token.substring(7) // Extract email from token (assuming it's the email)
        if (email.isEmpty()) {
            return unauthorized(reason = "Email in token is empty")
        }
        return ok(data = service.getFcmTokensByEmail(email))
    }
}
