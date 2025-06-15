package com.lcaohoanq.sp.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiQuotable(val endpoint: String)
