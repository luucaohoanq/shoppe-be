package com.lcaohoanq.sp.bases

import com.lcaohoanq.sp.apis.MyApiResponseV2
import org.springframework.http.ResponseEntity

open class BaseController {

    fun <T> ok(message: String = "Success", data: T): ResponseEntity<MyApiResponseV2<T>> {
        return MyApiResponseV2.success(data)
    }

    fun <T> created(data: T): ResponseEntity<MyApiResponseV2<T>> {
        return MyApiResponseV2.created(data)
    }

    fun <T> created(): ResponseEntity<MyApiResponseV2<T>> {
        return MyApiResponseV2.created()
    }

    fun <T> updated(data: T): ResponseEntity<MyApiResponseV2<T>> {
        return MyApiResponseV2.updated(data)
    }

    fun <T> noContent(): ResponseEntity<MyApiResponseV2<T>> {
        return MyApiResponseV2.noContent()
    }

    fun <T> badRequest(reason: String): ResponseEntity<MyApiResponseV2<T>> {
        return MyApiResponseV2.badRequest(reason)
    }

    fun <T> notFound(reason: String): ResponseEntity<MyApiResponseV2<T>> {
        return MyApiResponseV2.notFound(reason)
    }

    fun <T> serverError(reason: String): ResponseEntity<MyApiResponseV2<T>> {
        return MyApiResponseV2.serverError(reason)
    }

    fun validationError(errors: Map<String, String>): ResponseEntity<MyApiResponseV2<Any>> {
        return MyApiResponseV2.validationError(errors)
    }

}
