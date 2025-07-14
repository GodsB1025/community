package com.smhrdc.community.domain.comment.service

import com.smhrdc.community.domain.comment.dto.CommentCreateRequest
import com.smhrdc.community.domain.comment.dto.CommentResponse
import com.smhrdc.community.domain.comment.dto.CommentUpdateRequest
import com.smhrdc.community.domain.comment.entity.Comment
import com.smhrdc.community.domain.comment.repository.CommentRepository
import com.smhrdc.community.domain.post.repository.PostRepository
import com.smhrdc.community.domain.user.repository.UserRepository
import com.smhrdc.community.global.config.auth.UserDetailsImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createComment(postId: Long, request: CommentCreateRequest, userDetails: UserDetailsImpl): Long {
        val post = postRepository.findById(postId).orElseThrow { IllegalArgumentException("게시글 없음") }
        val user = userRepository.findByEmail(userDetails.username)!!

        val parentComment = request.parentId?.let {
            commentRepository.findById(it).orElseThrow { IllegalArgumentException("부모 댓글 없음") }
        }

        val comment = Comment(
            content = request.content,
            post = post,
            user = user,
            parent = parentComment
        )
        return commentRepository.save(comment).id
    }

    @Transactional(readOnly = true)
    fun getCommentsByPost(postId: Long): List<CommentResponse> {
        val comments = commentRepository.findByPostIdAndParentIsNull(postId)
        return comments.map { CommentResponse.from(it) }
    }

    @Transactional
    fun updateComment(commentId: Long, request: CommentUpdateRequest, userDetails: UserDetailsImpl) {
        val comment = commentRepository.findById(commentId).orElseThrow { IllegalArgumentException("댓글 없음") }
        if (comment.user.email != userDetails.username) throw IllegalArgumentException("권한 없음")
        comment.update(request.content)
    }

    @Transactional
    fun deleteComment(commentId: Long, userDetails: UserDetailsImpl) {
        val comment = commentRepository.findById(commentId).orElseThrow { IllegalArgumentException("댓글 없음") }
        if (comment.user.email != userDetails.username) throw IllegalArgumentException("권한 없음")
        commentRepository.delete(comment)
    }
}