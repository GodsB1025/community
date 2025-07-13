package com.smhrdc.community.domain.post.service

import com.smhrdc.community.domain.post.dto.PostCreateRequest
import com.smhrdc.community.domain.post.dto.PostDetailResponse
import com.smhrdc.community.domain.post.dto.PostSimpleResponse
import com.smhrdc.community.domain.post.dto.PostUpdateRequest
import com.smhrdc.community.domain.post.entity.Post
import com.smhrdc.community.domain.post.repository.PostRepository
import com.smhrdc.community.domain.user.repository.UserRepository
import com.smhrdc.community.global.config.auth.UserDetailsImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createPost(request: PostCreateRequest, userDetails: UserDetailsImpl): Long {
        val user = userRepository.findByEmail(userDetails.username)
            ?: throw IllegalArgumentException("존재하지 않는 사용자입니다.")

        val post = Post(
            title = request.title,
            content = request.content,
            user = user
        )
        val savedPost = postRepository.save(post)
        return savedPost.id
    }

    @Transactional(readOnly = true)
    fun getPostList(pageable: Pageable): Page<PostSimpleResponse> {
        return postRepository.findAllWithUser(pageable).map { PostSimpleResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getPost(postId: Long): PostDetailResponse {
        val post = postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 게시글입니다.") }
        return PostDetailResponse.from(post)
    }

    @Transactional(readOnly = true)
    fun getUsersPost(userDetails: UserDetailsImpl, pageable: Pageable): Page<PostSimpleResponse> {
        val user = userRepository.findByEmail(userDetails.username)
            ?: throw IllegalArgumentException("존재하지 않는 사용자입니다.")

        return postRepository.findByUserId(user.id, pageable).map { PostSimpleResponse.from(it) }
    }

    @Transactional
    fun updatePost(postId: Long, request: PostUpdateRequest, userDetails: UserDetailsImpl) {
        val post = postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 게시글입니다.") }

        if (post.user.email != userDetails.username) {
            throw IllegalArgumentException("게시글 수정 권한이 없습니다.")
        }

        post.title = request.title
        post.content = request.content
    }

    @Transactional
    fun deletePost(postId: Long, userDetails: UserDetailsImpl) {
        val post = postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 게시글입니다.") }

        if (post.user.email != userDetails.username) {
            throw IllegalArgumentException("게시글 삭제 권한이 없습니다.")
        }

        postRepository.delete(post)
    }
}