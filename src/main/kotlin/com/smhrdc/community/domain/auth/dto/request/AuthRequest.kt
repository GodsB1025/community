package com.smhrdc.community.domain.auth.dto.request

import io.jsonwebtoken.security.Password
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignupRequest(
    @field:NotBlank(message = "이름은 필수입니다.")
    val name: String,

    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    @field:NotBlank(message = "이메일은 필수입니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String
)

data class LoginRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String
)