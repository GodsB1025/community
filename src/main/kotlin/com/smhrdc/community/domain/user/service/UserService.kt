package com.smhrdc.community.domain.user.service

import com.smhrdc.community.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional
    fun leave(username: String): Int {
        return userRepository.deleteUserByEmail(username)
    }
}