package com.smhrdc.community.global.config.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 1. 요청의 쿠키에서 Access Token 추출
        val token = resolveToken(request)

        // 2. 토큰 유효성 검증
        if (token != null && tokenProvider.validateToken(token)) {
            // 토큰이 유효할 경우, 토큰에서 사용자 정보(username)를 가져옴
            val username = tokenProvider.getUsername(token)
            // UserDetailsService를 통해 사용자 정보를 DB에서 조회
            val userDetails = userDetailsService.loadUserByUsername(username)

            // 3. 인증 정보 객체 생성 및 SecurityContext에 저장
            val authentication = UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.authorities
            )
            SecurityContextHolder.getContext().authentication = authentication
        }

        // 4. 다음 필터로 요청 전달
        filterChain.doFilter(request, response)
    }

    /**
     * HttpServletRequest에서 Access Token을 추출하는 메서드
     */
    private fun resolveToken(request: HttpServletRequest): String? {
        val cookies = request.cookies ?: return null
        return cookies.find { it.name == "accessToken" }?.value
    }
}