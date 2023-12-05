package com.kt.bbs.service.dto

data class CommentUpdateRequestDto(
    val commentId: Long,
    val content: String,
    val updatedBy: String,
)
