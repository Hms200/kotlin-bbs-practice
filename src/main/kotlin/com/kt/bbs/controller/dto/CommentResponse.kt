package com.kt.bbs.controller.dto

data class CommentResponse(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String,
    val updatedAt: String,
)
