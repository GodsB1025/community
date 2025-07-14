package com.smhrdc.community.domain.comment.dto

import com.smhrdc.community.domain.comment.entity.Comment
import jakarta.validation.constraints.NotBlank

// 댓글 생성 요청
data class CommentCreateRequest(
    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String,
    val parentId: Long? // 대댓글일 경우 부모 댓글 ID
)

// 댓글 수정 요청
data class CommentUpdateRequest(
    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String
)

// 댓글 응답 (계층 구조 포함)
data class CommentResponse(
    val id: Long,
    val content: String,
    val authorName: String,
    val children: List<CommentResponse> = listOf() // 자식 댓글 목록
) {
    companion object {

        fun from(comment: Comment): CommentResponse {
            return CommentResponse(
                id = comment.id,
                content = comment.content,
                authorName = comment.user.name,
                children = comment.children.map { from(it) }
            )
        }
    }
}