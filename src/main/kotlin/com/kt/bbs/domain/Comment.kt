package com.kt.bbs.domain

import com.kt.bbs.exception.CommentCreatorMismatchException
import com.kt.bbs.exception.CommentIdMismatchException
import com.kt.bbs.service.dto.CommentUpdateRequestDto
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Comment(
    content: String,
    post: Post,
    createdBy: String,
) : BaseEntity(createdBy) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post = post
        protected set

    fun update(request: CommentUpdateRequestDto) {
        if (this.id != request.commentId) throw CommentIdMismatchException()
        if (this.createdBy != request.updatedBy) throw CommentCreatorMismatchException()
        this.content = request.content
        super.update(request.updatedBy)
    }
}
