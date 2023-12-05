package com.kt.bbs.controller.dto

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)
