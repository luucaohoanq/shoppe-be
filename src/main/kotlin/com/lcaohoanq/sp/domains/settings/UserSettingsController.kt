package com.lcaohoanq.sp.domains.settings

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.domains.user.UserPort
import com.lcaohoanq.sp.extension.toUserSettingsResponse
import com.lcaohoanq.sp.repositories.UserSettingsRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user-settings")
@Tag(name = "user-settings", description = "User settings APIs")
class UserSettingsController(
    private val userSettingsRepository: UserSettingsRepository
) : BaseController() {

    @GetMapping("")
    @Operation(summary = "Get settings of user by id")
    fun getDetailSettingsOfUser(@RequestParam id: Long): ResponseEntity<MyApiResponse<UserPort.UserSettingsResponse>> {
        val data = userSettingsRepository.findByUserId(id).toUserSettingsResponse()
        return ok(message = "User settings of $id has been successfully retrieved", data = data)
    }

}
