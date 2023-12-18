package com.kt.bbs.controller.dto

import org.springframework.web.bind.annotation.RequestParam

data class PostSearchRequest(
    @RequestParam
    val title: String? = null,
    @RequestParam
    val createdBy: String? = null,
    @RequestParam
    val tag: String? = null,
)
