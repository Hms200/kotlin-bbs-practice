package com.kt.bbs.controller.dto

data class PostDetailResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
    val comments: List<CommentResponse> = emptyList(),
    val tags: List<String> = emptyList(),
)
