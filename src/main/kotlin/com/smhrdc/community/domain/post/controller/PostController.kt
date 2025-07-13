package com.smhrdc.community.domain.post.controller

import com.smhrdc.community.domain.post.dto.*
import com.smhrdc.community.domain.post.service.PostService
import com.smhrdc.community.global.common.ApiResponseMessage
import com.smhrdc.community.global.config.auth.UserDetailsImpl
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService
) {
    @ApiResponseMessage("게시글 작성에 성공했습니다.")
    @PostMapping
    fun createPost(
        @Valid @RequestBody request: PostCreateRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): ResponseEntity<Unit> {
        val postId = postService.createPost(request, userDetails)
        return ResponseEntity.created(URI.create("/api/posts/$postId")).build()
    }

    @GetMapping
    fun getPostList(
        @PageableDefault(size = 10, sort = ["createdAt"]) pageable: Pageable
    ): Page<PostSimpleResponse> {
        return postService.getPostList(pageable)
    }

    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long): PostDetailResponse {
        return postService.getPost(postId)
    }

    @GetMapping("/my-posts")
    fun getMyPost(@AuthenticationPrincipal userDetails: UserDetailsImpl,
                  @PageableDefault(size = 10, sort = ["createdAt"]
    ) pageable: Pageable): Page<PostSimpleResponse> {

        return postService.getUsersPost(userDetails, pageable)
    }

    @ApiResponseMessage("게시글 수정에 성공했습니다.")
    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @Valid @RequestBody request: PostUpdateRequest,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ) {
        postService.updatePost(postId, request, userDetails)
    }

    @ApiResponseMessage("게시글 삭제에 성공했습니다.")
    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ) {
        postService.deletePost(postId, userDetails)
    }
}