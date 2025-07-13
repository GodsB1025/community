package com.smhrdc.community.domain.auth.controller

import com.smhrdc.community.domain.auth.dto.request.LoginRequest
import com.smhrdc.community.domain.auth.dto.request.SignupRequest
import com.smhrdc.community.domain.auth.dto.response.SignupResponse
import com.smhrdc.community.domain.auth.service.AuthService
import com.smhrdc.community.global.common.ApiResponse
import com.smhrdc.community.global.common.ApiResponseMessage
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @ApiResponseMessage("회원가입에 성공했습니다.")
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignupRequest): SignupResponse {

        return authService.signup(request)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        val tokens = authService.login(request)

        val accessTokenCookie = ResponseCookie.from("accessToken", tokens["accessToken"]!!)
            .path("/")
            .httpOnly(true)
            .secure(true) // HTTPS 환경에서만 쿠키 전송
            .maxAge(3600)
            .build()

        val refreshTokenCookie = ResponseCookie.from("refreshToken", tokens["refreshToken"]!!)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .maxAge(604800)
            .build()

        return ResponseEntity.ok()
            .header("Set-Cookie", accessTokenCookie.toString())
            .header("Set-Cookie", refreshTokenCookie.toString())
            .body(ApiResponse.success(null, "로그인에 성공했습니다."))
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<ApiResponse<Unit>> {
        // Access Token 쿠키 삭제
        val accessTokenCookie = ResponseCookie.from("accessToken", "")
            .path("/")
            .httpOnly(true)
            .secure(true)
            .maxAge(0)
            .build()

        // Refresh Token 쿠키 삭제
        val refreshTokenCookie = ResponseCookie.from("refreshToken", "")
            .path("/")
            .httpOnly(true)
            .secure(true)
            .maxAge(0)
            .build()

        return ResponseEntity.ok()
            .header("Set-Cookie", accessTokenCookie.toString())
            .header("Set-Cookie", refreshTokenCookie.toString())
            .body(ApiResponse.success(null, "로그아웃에 성공했습니다."))
    }

    @PostMapping("/refresh")
    fun refresh(
        @CookieValue("refreshToken") refreshToken: String?,
        response: HttpServletResponse
    ): ResponseEntity<ApiResponse<Unit>> {
        if (refreshToken == null) {
            // ApiResponse 형식으로 에러 응답 통일
            val apiResponse = ApiResponse.error<Unit>("Refresh Token이 없습니다.")
            return ResponseEntity.status(401).body(apiResponse)
        }

        try {
            val newAccessToken = authService.refreshAccessToken(refreshToken)

            val accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(3600) // 1시간
                .build()

            response.addHeader("Set-Cookie", accessTokenCookie.toString())

            return ResponseEntity.ok(ApiResponse.success(null, "Access Token이 재발급되었습니다."))
        } catch (e: IllegalArgumentException) {
            val apiResponse = ApiResponse.error<Unit>(e.message)
            return ResponseEntity.status(401).body(apiResponse)
        }
    }
}