package com.smhrdc.community.global.config.auth

import com.smhrdc.community.domain.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(private val user: User) : UserDetails {

    // 사용자의 권한 목록을 반환
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList()
    }

    // 사용자의 비밀번호를 반환
    override fun getPassword(): String {
        return user.password
    }

    // 사용자의 고유 식별자(이메일)를 반환
    override fun getUsername(): String {
        return user.email
    }

    // 계정이 만료되지 않았는지 확인
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    // 계정이 잠기지 않았는지 확인
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    // 자격 증명(비밀번호)이 만료되지 않았는지 확인
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    // 계정이 활성화되었는지 확인
    override fun isEnabled(): Boolean {
        return true
    }

    fun getName(): String {
        return user.name
    }
}