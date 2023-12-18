package com.kt.bbs.service.dto

import com.kt.bbs.domain.Post

data class PostCreateRequestDto(
    val title: String,
    val content: String,
    val createdBy: String,
    val tags: List<String> = emptyList(),
)

fun PostCreateRequestDto.toEntity() = Post(
    title = title,
    content = content,
    createdBy = createdBy,
    tags = tags,
)
