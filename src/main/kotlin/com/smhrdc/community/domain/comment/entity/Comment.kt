package com.smhrdc.community.domain.comment.entity

import com.smhrdc.community.domain.post.entity.Post
import com.smhrdc.community.domain.user.entity.User
import com.smhrdc.community.global.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "comments")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Lob
    @Column(nullable = false)
    var content: String,

    // 연관된 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    // 부모 댓글 (Self-referencing)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Comment? = null, // 최상위 댓글은 부모가 없음 (nullable)

    // 자식 댓글 목록
    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    val children: MutableList<Comment> = mutableListOf()

) : BaseEntity() {
    // 댓글 내용 수정을 위한 편의 메서드
    fun update(content: String) {
        this.content = content
    }
}