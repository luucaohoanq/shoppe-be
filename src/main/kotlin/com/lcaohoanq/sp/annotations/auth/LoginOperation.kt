package com.lcaohoanq.sp.annotations.auth

import com.lcaohoanq.sp.dto.AuthPort.AuthRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@Operation(
    summary = "🚀 Login with email and password",
    requestBody = RequestBody(
        description = "Login credentials",
        required = true,
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = AuthRequest::class),
                examples = [ExampleObject(
                    name = "Demo User",
                    summary = "Example login",
                    description = "Sample credentials for testing",
                    externalValue = "classpath:/swagger/examples/login.json"
                )]
            )
        ]
    )
)
annotation class LoginOperation
