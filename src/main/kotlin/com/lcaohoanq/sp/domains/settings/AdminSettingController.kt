package com.lcaohoanq.sp.domains.settings

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.entities.SystemSetting
import com.lcaohoanq.sp.extension.toAdminSettingResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.prefix}/admin-settings")
@Tag(name = "admin-settings", description = "⚙️ Admin Settings API - Manage system-wide settings")
class AdminSettingController(
    private val adminSettingService: IAdminSettingService
) : BaseController() {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Get all admin settings",
        description = "Retrieve all system-wide admin settings. Requires ADMIN or MANAGER role."
    )
    fun getAllSettings(): ResponseEntity<MyApiResponse<List<AdminSettingPort.AdminSettingResponse>>> {
        val settings = adminSettingService.getAllSettings().map { it.toAdminSettingResponse() }
        return MyApiResponse.success(data = settings)
    }

    @GetMapping("/{key}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(
        summary = "Get admin setting by key",
        description = "Retrieve a specific admin setting by its key. Requires ADMIN or MANAGER role."
    )
    fun getSettingByKey(@PathVariable key: String): ResponseEntity<MyApiResponse<AdminSettingPort.AdminSettingResponse?>> {
        val setting = adminSettingService.getSettingByKey(key)
        return if (setting != null) {
            MyApiResponse.success(data = setting.toAdminSettingResponse())
        } else {
            MyApiResponse.notFound("Setting with key '$key' not found")
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create admin setting",
        description = "Create a new admin setting. Requires ADMIN role."
    )
    fun createSetting(@RequestBody setting: SystemSetting): ResponseEntity<MyApiResponse<AdminSettingPort.AdminSettingResponse>> {
        val existingSetting = adminSettingService.getSettingByKey(setting.settingKey ?: "")
        if (existingSetting != null) {
            return MyApiResponse.badRequest("Setting with key '${setting.settingKey}' already exists")
        }
        
        val savedSetting = adminSettingService.createSetting(setting)
        return MyApiResponse.created(data = savedSetting.toAdminSettingResponse())
    }

    @PutMapping("/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update admin setting",
        description = "Update an existing admin setting by its key. Requires ADMIN role."
    )
    fun updateSetting(
        @PathVariable key: String,
        @RequestBody newValue: AdminSettingPort.AdminSettingUpdateRequest
    ): ResponseEntity<MyApiResponse<AdminSettingPort.AdminSettingResponse>> {
        val updatedSetting = adminSettingService.updateSetting(key, newValue.value)
        return if (updatedSetting != null) {
            MyApiResponse.success(updatedSetting.toAdminSettingResponse())
        } else {
            MyApiResponse.notFound("Setting with key '$key' not found")
        }
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete admin setting",
        description = "Delete an admin setting by its key. Requires ADMIN role."
    )
    fun deleteSetting(@PathVariable key: String): ResponseEntity<MyApiResponse<Void>> {
        val deleted = adminSettingService.deleteSetting(key)
        return if (deleted) {
            MyApiResponse.noContent()
        } else {
            MyApiResponse.notFound("Setting with key '$key' not found")
        }
    }
}

// AdminSettingUpdateRequest moved to AdminSettingPort