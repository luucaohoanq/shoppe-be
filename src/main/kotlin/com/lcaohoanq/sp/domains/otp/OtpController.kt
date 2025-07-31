package com.lcaohoanq.sp.domains.otp

import com.lcaohoanq.sp.apis.MyApiResponseV2
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.dto.OtpPort
import com.lcaohoanq.sp.repositories.UserRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.prefix}/otps")
@Tag(name = "otps", description = "Operations related to OTP")
class OtpController(
    private val userRepository: UserRepository,
    private val otpService: IOtpService
) : BaseController() {

    @GetMapping("")
    @PreAuthorize("permitAll()")
    fun getAll(): ResponseEntity<MyApiResponseV2<List<OtpPort.OtpRes>>> {
        val otps = otpService.getAllOtps()
        return ok(
            message = "Get all OTPs successfully",
            data = otps
        )
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    fun createOtp(@RequestBody otp: OtpPort.OtpReq): ResponseEntity<MyApiResponseV2<String>> {
        val user = userRepository.findByEmail(otp.email).orElse(null)
        if (user != null) {
            otpService.createOtpFor(user, otp)
            return ok(
                message = "OTP created successfully",
                data = "OTP has been sent to ${otp.email}"
            )
        } else {
            return notFound("User with email ${otp.email} not found")
        }
    }

}
