package com.smhrdc.community.domain.comment.repository

import com.smhrdc.community.domain.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommentRepository : JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.post.id = :postId AND c.parent IS NULL ORDER BY c.createdAt ASC")
    fun findByPostIdAndParentIsNull(postId: Long): List<Comment>
}