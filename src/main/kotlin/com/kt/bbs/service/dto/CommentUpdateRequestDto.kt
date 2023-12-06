package com.kt.bbs.service.dto

import com.kt.bbs.controller.dto.CommentUpdateRequest

data class CommentUpdateRequestDto(
    val commentId: Long,
    val content: String,
    val updatedBy: String,
)

fun CommentUpdateRequest.toDto(commentId: Long): CommentUpdateRequestDto {
    return CommentUpdateRequestDto(
        commentId = commentId,
        content = this.content,
        updatedBy = this.updatedBy,
    )
}
