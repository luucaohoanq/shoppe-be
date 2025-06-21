package com.lcaohoanq.sp.domains.otp

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.dto.OtpPort
import com.lcaohoanq.sp.repositories.UserRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.prefix}/otps")
@Tag(name = "otps", description = "Operations related to OTP")
class OtpController(
    private val userRepository: UserRepository,
    private val otpService: IOtpService
) {

    @GetMapping("")
    @PreAuthorize("permitAll()")
    fun getAll(): ResponseEntity<MyApiResponse<List<OtpPort.OtpRes>>> {
        val otps = otpService.getAllOtps()
        return ResponseEntity.ok(MyApiResponse(
            message = "Get All Otps successfully",
            data = otps
        ))
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    fun createOtp(@RequestBody otp: OtpPort.OtpReq): ResponseEntity<String> {
        val user = userRepository.findByEmail(otp.email)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found")
        otpService.createOtpFor(user, otp)
        return ResponseEntity.ok("OTP created successfully")
    }

}
