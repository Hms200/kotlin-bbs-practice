package com.kt.bbs.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Tag(
    name: String,
    createdBy: String,
    post: Post,
) : BaseEntity(createdBy) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column
    var name: String = name
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post = post
        protected set

}
