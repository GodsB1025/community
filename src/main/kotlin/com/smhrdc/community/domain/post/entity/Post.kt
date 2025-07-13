package com.smhrdc.community.domain.post.entity

import com.smhrdc.community.domain.user.entity.User
import com.smhrdc.community.global.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "posts")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @Lob
    @Column(nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User

) : BaseEntity()