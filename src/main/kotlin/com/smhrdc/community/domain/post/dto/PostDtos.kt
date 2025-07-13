package com.smhrdc.community.domain.post.dto

import com.smhrdc.community.domain.post.entity.Post
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

// 게시글 생성 요청 DTO
data class PostCreateRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String
)

// 게시글 수정 요청 DTO
data class PostUpdateRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String
)

// 게시글 목록 조회 응답 DTO (간단 정보)
data class PostSimpleResponse(
    val id: Long,
    val title: String,
    val authorName: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(post: Post): PostSimpleResponse {
            return PostSimpleResponse(
                id = post.id,
                title = post.title,
                authorName = post.user.name,
                createdAt = post.createdAt
            )
        }
    }
}

// 게시글 상세 조회 응답 DTO
data class PostDetailResponse(
    val id: Long,
    val title: String,
    val content: String,
    val authorName: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
) {
    companion object {
        fun from(post: Post): PostDetailResponse {
            return PostDetailResponse(
                id = post.id,
                title = post.title,
                content = post.content,
                authorName = post.user.name,
                createdAt = post.createdAt,
                modifiedAt = post.modifiedAt
            )
        }
    }
}