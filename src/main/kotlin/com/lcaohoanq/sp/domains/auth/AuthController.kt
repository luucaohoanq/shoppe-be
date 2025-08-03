package com.lcaohoanq.sp.domains.auth

import com.lcaohoanq.sp.annotations.auth.LoginApiResponses
import com.lcaohoanq.sp.annotations.auth.LoginOperation
import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.domains.user.IUserService
import com.lcaohoanq.sp.dto.AuthPort
import com.lcaohoanq.sp.dto.TokenPort
import com.lcaohoanq.sp.exceptions.MethodArgumentNotValidException
import com.lcaohoanq.sp.utils.AuthenticationUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import mu.KotlinLogging
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*


@Tag(name = "auth", description = "\uD83D\uDD12 Auth API")
@RestController
@RequestMapping("\${api.prefix}/auth")
class AuthController(
    private val authService: IAuthService,
    private val userService: IUserService,
    private val request: HttpServletRequest
) : BaseController() {

    private val log = KotlinLogging.logger {}

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER', 'ROLE_STAFF', 'ROLE_CUSTOMER')")
    @Operation(
        summary = "Test authenticated endpoint",
        description = "Test endpoint to verify authentication is working",
        security = [SecurityRequirement(name = "keycloak")]
    )
    fun test(): ResponseEntity<Any> {
        log.info { "Test endpoint hit" }
        
        val userId = AuthenticationUtils.getUserId()
        val userEmail = AuthenticationUtils.getUserEmail()
        val authentication = SecurityContextHolder.getContext().authentication
        
        val responseData = mapOf(
            "userId" to userId,
            "userEmail" to userEmail,
            "authenticationType" to authentication.javaClass.simpleName,
            "authorities" to authentication.authorities.map { it.authority },
            "principalName" to authentication.name
        )
        
        return ResponseEntity.ok(responseData)
    }

    @LoginOperation
    @LoginApiResponses
    @PostMapping("/login")
    fun login(@RequestBody req: AuthPort.AuthRequest): ResponseEntity<MyApiResponse<LoginResult>> =
        ok(message = "Login successfully", data = authService.login(req))


    @Operation(summary = "Register", description = "Register")
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody user: AuthPort.SignUpReq,
        bindingResult: BindingResult
    ): ResponseEntity<MyApiResponse<Nothing?>> {  // Change return type to match actual
        if (bindingResult.hasErrors()) throw MethodArgumentNotValidException(bindingResult)

        authService.register(user)
        return ok(
            message = "Register successfully",
            data = null
        )
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/refresh-token")
    @Operation(summary = "Send refresh token to get new access token")
    fun refreshToken(
        @Valid @RequestBody refreshTokenDTO: TokenPort.RefreshTokenDTO,
        result: BindingResult
    ): ResponseEntity<MyApiResponse<AuthPort.AuthResponse>> {  // Change from MyApiResponse to MyApiResponseV2
        if (result.hasErrors()) throw MethodArgumentNotValidException(result)
        return ok(
            message = "Refresh token successfully",
            data = authService.refreshToken(refreshTokenDTO)
        )
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER', 'ROLE_STAFF')")
    @PostMapping("/logout")
    @Operation(
        summary = "Logout from the system",
        description = "Invalidate the current JWT token",
        security = [SecurityRequirement(name = "JavaInUseSecurityScheme")]
    )
    fun logout(): ResponseEntity<MyApiResponse<Nothing?>> {
        val authorizationHeader: String = request.getHeader("Authorization")

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw IllegalArgumentException("Authorization header must start with 'Bearer '")
        }
        val token = authorizationHeader.substring(7)

        val userDetails = SecurityContextHolder.getContext()
            .authentication.principal as UserDetails
        val user = userService.findByEmail(userDetails.username)
            ?: throw IllegalArgumentException("User not found")

        authService.logout(token, user) //revoke token

        return ok(message = "Logout successfully", data = null)
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/generate-token-from-email")
    @Operation(
        summary = "Generate token from email",
        description = "Generate token from email",
    )
    fun generateTokenFromEmail(
        @Valid @RequestBody data: AuthPort.VerifyEmailReq,
        bindingResult: BindingResult
    ): ResponseEntity<MyApiResponse<String>> {

        if (bindingResult.hasErrors()) throw MethodArgumentNotValidException(bindingResult)

        val response = authService.generateTokenFromEmail(data.email)
        return ok(message = "Generate token successfully", data= response)
    }

    @PreAuthorize("permitAll()")
    @PatchMapping("/change-password")
    @Operation(
        summary = "Change password",
        description = "Change password",
    )
    fun changePassword(
        @Valid @RequestBody data: AuthPort.ChangePasswordReq,
        bindingResult: BindingResult
    ): ResponseEntity<MyApiResponse<Nothing?>> {
        if (bindingResult.hasErrors()) throw MethodArgumentNotValidException(bindingResult)

        authService.changePassword(data)
        return ok(message = "Change password successfully", data = null)
    }


    @GetMapping("/verify-account")
    @Operation(
        summary = "Verify account",
        description = "This link will sent via email, user press to verify account",
    )
    fun verifyAccount(@RequestParam token: String): ResponseEntity<MyApiResponse<Nothing?>> {
        authService.verifyAccount(token)
        return ok(message = "Verify account successfully" , data = null)
    }

}
