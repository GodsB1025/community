package com.smhrdc.community.domain.user.controller

import com.smhrdc.community.domain.user.dto.response.InfoResponse
import com.smhrdc.community.domain.user.repository.UserRepository
import com.smhrdc.community.domain.user.service.UserService
import com.smhrdc.community.global.common.ApiResponseMessage
import com.smhrdc.community.global.config.auth.UserDetailsImpl
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository
) {
    @GetMapping("/me")
    fun getMyInfo(@AuthenticationPrincipal userDetails:UserDetailsImpl
    ): InfoResponse {
        return InfoResponse(
            email = userDetails.username,
            name = userDetails.getName()
        )
    }

    @ApiResponseMessage("회원탈퇴를 성공했습니다.")
    @DeleteMapping("/me")
    fun leave(@AuthenticationPrincipal userDetails: UserDetailsImpl): Unit? {
        userService.leave(userDetails.username)
        return null
    }
}