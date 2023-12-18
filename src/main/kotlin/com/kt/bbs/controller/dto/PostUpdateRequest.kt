package com.kt.bbs.controller.dto

import com.kt.bbs.service.dto.PostUpdateRequestDto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
    val tags: List<String>? = null,
)

fun PostUpdateRequest.toDto() = PostUpdateRequestDto(
    title = title,
    content = content,
    updatedBy = updatedBy,
    tags = tags,
)
