package com.kt.bbs.service.dto

import com.kt.bbs.controller.dto.CommentCreateRequest
import com.kt.bbs.domain.Comment
import com.kt.bbs.domain.Post

data class CommentCreateRequestDto(
    val postId: Long,
    val content: String,
    val createdBy: String,
) {
    fun toEntity(post: Post): Comment = Comment(
        content = content,
        post = post,
        createdBy = createdBy,
    )
}

fun CommentCreateRequest.toDto(postId: Long, request: CommentCreateRequest): CommentCreateRequestDto =
    CommentCreateRequestDto(
        postId = postId,
        content = request.content,
        createdBy = request.createdBy,
    )
