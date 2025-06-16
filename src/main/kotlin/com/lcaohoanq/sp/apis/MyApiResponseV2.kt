package com.lcaohoanq.sp.apis

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.Instant

sealed interface MyApiResponseV2<T> {
    val statusCode: Int
    val message: String
    val timestamp: Instant

    // Success data class
    data class Success<T>(
        @JsonIgnore override val statusCode: Int,
        override val message: String,
        val data: T?,
        @JsonIgnore override val timestamp: Instant
    ) : MyApiResponseV2<T>

    // Error data class with path tracking
    data class Error<T>(
        override val statusCode: Int,
        override val message: String,
        val reason: String,
        val path: String,
        override val timestamp: Instant
    ) : MyApiResponseV2<T>

    // ValidationError data class with path tracking
    data class ValidationError<T>(
        override val statusCode: Int,
        override val message: String,
        val fieldErrors: Map<String, String>,
        val path: String,
        override val timestamp: Instant
    ) : MyApiResponseV2<T>

    companion object {
        // Helper method to get current request path
        private fun getCurrentPath(): String {
            return try {
                val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                attributes.request.requestURI
            } catch (e: IllegalStateException) {
                "unknown"
            }
        }

        // Success response methods
        fun <T> success(data: T): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.ok(
                Success(
                    statusCode = 200,
                    message = "Success",
                    data = data,
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> created(data: T): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                Success(
                    statusCode = 201,
                    message = "Created successfully",
                    data = data,
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> created(): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                Success(
                    statusCode = 201,
                    message = "Created successfully",
                    data = null,
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> updated(data: T): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.ok(
                Success(
                    statusCode = 200,
                    message = "Updated successfully",
                    data = data,
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> updated(): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.ok(
                Success(
                    statusCode = 200,
                    message = "Updated successfully",
                    data = null,
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> noContent(): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                Success(
                    statusCode = 204,
                    message = "No Content",
                    data = null,
                    timestamp = Instant.now()
                )
            )
        }

        // Error response methods
        fun <T> badRequest(reason: String): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.badRequest().body(
                Error(
                    statusCode = 400,
                    message = "Bad Request",
                    reason = reason,
                    path = getCurrentPath(),
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> badRequest(reason: String, path: String): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.badRequest().body(
                Error(
                    statusCode = 400,
                    message = "Bad Request",
                    reason = reason,
                    path = path,
                    timestamp = Instant.now()
                )
            )
        }

        fun validationError(errors: Map<String, String>): ResponseEntity<MyApiResponseV2<Any>> {
            return ResponseEntity.badRequest().body(
                ValidationError(
                    statusCode = 400,
                    message = "Validation failed",
                    fieldErrors = errors,
                    path = getCurrentPath(),
                    timestamp = Instant.now()
                )
            )
        }

        fun validationError(errors: Map<String, String>, path: String): ResponseEntity<MyApiResponseV2<Any>> {
            return ResponseEntity.badRequest().body(
                ValidationError(
                    statusCode = 400,
                    message = "Validation failed",
                    fieldErrors = errors,
                    path = path,
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> notFound(reason: String): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Error(
                    statusCode = 404,
                    message = "Not Found",
                    reason = reason,
                    path = getCurrentPath(),
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> notFound(reason: String, path: String): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Error(
                    statusCode = 404,
                    message = "Not Found",
                    reason = reason,
                    path = path,
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> unauthorized(reason: String): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Error(
                    statusCode = 401,
                    message = "Unauthorized",
                    reason = reason,
                    path = getCurrentPath(),
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> serverError(reason: String): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Error(
                    statusCode = 500,
                    message = "Internal Server Error",
                    reason = reason,
                    path = getCurrentPath(),
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> error(status: HttpStatus, message: String, reason: String): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.status(status).body(
                Error(
                    statusCode = status.value(),
                    message = message,
                    reason = reason,
                    path = getCurrentPath(),
                    timestamp = Instant.now()
                )
            )
        }

        fun <T> error(status: HttpStatus, message: String, reason: String, path: String): ResponseEntity<MyApiResponseV2<T>> {
            return ResponseEntity.status(status).body(
                Error(
                    statusCode = status.value(),
                    message = message,
                    reason = reason,
                    path = path,
                    timestamp = Instant.now()
                )
            )
        }
    }
}