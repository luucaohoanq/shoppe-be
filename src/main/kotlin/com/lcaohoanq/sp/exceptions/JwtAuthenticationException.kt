package com.lcaohoanq.sp.exceptions

class JwtAuthenticationException : RuntimeException {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
