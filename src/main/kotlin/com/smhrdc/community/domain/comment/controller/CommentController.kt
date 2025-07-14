package com.smhrdc.community.domain.comment.controller

import com.smhrdc.community.domain.comment.dto.CommentCreateRequest
import com.smhrdc.community.domain.comment.dto.CommentResponse
import com.smhrdc.community.domain.comment.dto.CommentUpdateRequest
import com.smhrdc.community.domain.comment.service.CommentService
import com.smhrdc.community.global.config.auth.UserDetailsImpl
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api")
class CommentController(private val commentService: CommentService) {

    @PostMapping("/posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @Valid @RequestBody request: CommentCreateRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<Unit> {
        val commentId = commentService.createComment(postId, request, userDetails)
        return ResponseEntity.created(URI.create("/api/comments/$commentId")).build()
    }

    @GetMapping("/posts/{postId}/comments")
    fun getComments(@PathVariable postId: Long): List<CommentResponse> {
        return commentService.getCommentsByPost(postId)
    }

    @PutMapping("/comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @Valid @RequestBody request: CommentUpdateRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<Unit> {
        commentService.updateComment(commentId, request, userDetails)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<Unit> {
        commentService.deleteComment(commentId, userDetails)
        return ResponseEntity.noContent().build()
    }
}