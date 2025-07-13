package com.smhrdc.community.domain.post.repository

import com.smhrdc.community.domain.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.user",
        countQuery = "SELECT COUNT(p) FROM Post p")
    fun findAllWithUser(pageable: Pageable): Page<Post>

    @Query(
        "SELECT p FROM Post p JOIN FETCH p.user u WHERE u.id = :userId",
        countQuery = "SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId"
    )
    fun findByUserId(userId: Long, pageable: Pageable): Page<Post>

}