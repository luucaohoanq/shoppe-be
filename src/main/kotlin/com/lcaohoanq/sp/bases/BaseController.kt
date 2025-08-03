package com.lcaohoanq.sp.bases

import com.lcaohoanq.sp.apis.MyApiResponse
import org.springframework.http.ResponseEntity

open class BaseController {

    fun <T> ok(message: String = "Success", data: T): ResponseEntity<MyApiResponse<T>> {
        return MyApiResponse.success(data)
    }

    fun <T> created(data: T): ResponseEntity<MyApiResponse<T>> {
        return MyApiResponse.created(data)
    }

    fun <T> created(): ResponseEntity<MyApiResponse<T>> {
        return MyApiResponse.created()
    }

    fun <T> updated(data: T): ResponseEntity<MyApiResponse<T>> {
        return MyApiResponse.updated(data)
    }

    fun <T> noContent(): ResponseEntity<MyApiResponse<T>> {
        return MyApiResponse.noContent()
    }

    fun <T> badRequest(reason: String): ResponseEntity<MyApiResponse<T>> {
        return MyApiResponse.badRequest(reason)
    }

    fun <T> notFound(reason: String): ResponseEntity<MyApiResponse<T>> {
        return MyApiResponse.notFound(reason)
    }

    fun <T> serverError(reason: String): ResponseEntity<MyApiResponse<T>> {
        return MyApiResponse.serverError(reason)
    }

    fun validationError(errors: Map<String, String>): ResponseEntity<MyApiResponse<Any>> {
        return MyApiResponse.validationError(errors)
    }

    fun <T> unauthorized(reason: String): ResponseEntity<MyApiResponse<T>> {
        return MyApiResponse.unauthorized(reason)
    }

}
