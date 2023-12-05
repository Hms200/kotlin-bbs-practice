package com.kt.bbs.domain

import com.kt.bbs.exception.PostCreatorMismatchException
import com.kt.bbs.service.dto.PostUpdateRequestDto
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Post(
    createdBy: String,
    title: String,
    content: String,
) : BaseEntity(createdBy) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var title: String = title
        protected set
    var content: String = content
        protected set

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = [CascadeType.ALL])
    var comments: MutableList<Comment> = mutableListOf()
        protected set

    fun update(requestDto: PostUpdateRequestDto) {
        if (this.createdBy != requestDto.updatedBy) throw PostCreatorMismatchException()
        this.title = requestDto.title
        this.content = requestDto.content
        super.update(requestDto.updatedBy)
    }
}
