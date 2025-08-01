package com.lcaohoanq.sp.annotations.auth

import com.lcaohoanq.sp.apis.MyApiResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
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
        responseCode = "201", description = "✅ Account created successfully", content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = MyApiResponse::class),
                examples = [
                    ExampleObject(
                        name = "Success Response", value = """
                    {
                      "statusCode": 201,
                      "message": "Created",
                      "data": null
                    }
                    
                    """
                    )
                ]
            )
        ]
    ), ApiResponse(
        responseCode = "400", description = "❌ Email already exists", content = [
            Content(
                mediaType = "application/json", examples = [
                    ExampleObject(
                        value = """
                    {
                      "statusCode": 400,
                      "message": "Email already exists",
                      "data": null
                    }
                    
                    """
                    )
                ]
            )
        ]
    )]
)
annotation class RegisterApiResponses 
