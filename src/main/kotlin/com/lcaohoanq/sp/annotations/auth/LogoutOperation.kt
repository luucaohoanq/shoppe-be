package com.lcaohoanq.sp.annotations.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "🚪 Logout and invalidate token", description = """
            **Logout and invalidate your current JWT token**
            
            This endpoint requires authentication. Make sure you're logged in first.
            After logout, you'll need to login again to access protected endpoints.
            
            """,
    security = [SecurityRequirement(name = "bearer-jwt")]
)
annotation class LogoutOperation 
