package com.smhrdc.community.domain.auth.dto.response

data class SignupResponse(
    val id: Long,
    val email: String,
    val name: String
)