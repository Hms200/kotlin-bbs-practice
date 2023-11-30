package com.kt.bbs.controller.dto

import com.kt.bbs.service.dto.PostCreateRequestDto

data class PostCreateRequest(
    val title: String,
    val content: String,
    val createdBy: String,
)

fun PostCreateRequest.toDto() = PostCreateRequestDto(
    title = this.title,
    content = this.content,
    createdBy = this.createdBy
)
