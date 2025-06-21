package com.lcaohoanq.sp.annotations.auth

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponses(
    value = [ApiResponse(
        responseCode = "204",
        description = "✅ Logout successful"
    ), ApiResponse(
        responseCode = "401",
        description = "❌ Unauthorized - Token invalid or missing"
    ), ApiResponse(responseCode = "404", description = "❌ Token not found")]
)
annotation class LogoutApiResponses 
