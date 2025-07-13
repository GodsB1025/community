package com.smhrdc.community.domain.auth.service

import com.smhrdc.community.domain.auth.dto.request.LoginRequest
import com.smhrdc.community.domain.auth.dto.request.SignupRequest
import com.smhrdc.community.domain.auth.dto.response.SignupResponse
import com.smhrdc.community.domain.user.entity.User
import com.smhrdc.community.domain.user.repository.UserRepository
import com.smhrdc.community.global.config.jwt.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: JwtTokenProvider
) {

    @Transactional
    fun signup(request: SignupRequest): SignupResponse {

        if (userRepository.existsByEmail(request.email)){
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }

        val encodedPassword = passwordEncoder.encode(request.password)

        val user = User(
            email = request.email,
            password = encodedPassword,
            name = request.name
        )
        val savedUser = userRepository.save(user)

        return SignupResponse(
            id = savedUser.id,
            email = savedUser.email,
            name = savedUser.name
        )
    }

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): Map<String, String> {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("이메일/비밀번호 를 확인하세요.")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("이메일/비밀번호 를 확인하세요.")
        }

        val accessToken = tokenProvider.createAccessToken(user.email)
        val refreshToken = tokenProvider.createRefreshToken(user.email)

        return mapOf(
            "accessToken" to accessToken,
            "refreshToken" to refreshToken
        )
    }

    fun refreshAccessToken(refreshToken: String): String {
        // 1. Refresh Token 유효성 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw IllegalArgumentException("유효하지 않은 Refresh Token 입니다.")
        }

        // 2. 토큰에서 사용자 정보(이메일) 추출
        val username = tokenProvider.getUsername(refreshToken)

        // 3. DB에 해당 사용자가 존재하는지 확인 (선택적이지만 더 안전함)
        userRepository.findByEmail(username)
            ?: throw IllegalArgumentException("존재하지 않는 사용자입니다.")

        // 4. 새로운 Access Token 생성
        return tokenProvider.createAccessToken(username)
    }
}