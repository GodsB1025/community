package com.smhrdc.community.global.common

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiResponseMessage(
    val value: String
)