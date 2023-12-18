package com.kt.bbs.service.dto

import com.kt.bbs.controller.dto.PostSearchRequest

data class PostSearchRequestDto(
    val title: String? = null,
    val createdBy: String? = null,
    val tag: String? = null,
)

fun PostSearchRequest.toDto() = PostSearchRequestDto(
    title = title,
    createdBy = createdBy,
    tag = tag,
)
