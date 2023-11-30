package com.kt.bbs.controller

import com.kt.bbs.controller.dto.PostCreateRequest
import com.kt.bbs.controller.dto.PostDetailResponse
import com.kt.bbs.controller.dto.PostSearchRequest
import com.kt.bbs.controller.dto.PostSummaryResponse
import com.kt.bbs.controller.dto.PostUpdateRequest
import com.kt.bbs.controller.dto.toDto
import com.kt.bbs.service.PostService
import com.kt.bbs.service.dto.toDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService,
) {

    @PostMapping("/posts")
    fun createPost(@RequestBody postCreateRequest: PostCreateRequest): Long {
        return postService.createPost(postCreateRequest.toDto())
    }

    @PutMapping("/posts/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestBody postModifyRequest: PostUpdateRequest,
    ): Long {
        return postService.updatePost(id, postModifyRequest.toDto())
    }

    @DeleteMapping("/posts/{id}")
    fun deletePost(
        @PathVariable id: Long,
        @RequestParam createdBy: String,
    ): Long {
        return postService.deletePost(id, createdBy)
    }

    @GetMapping("/posts/{id}")
    fun getPost(@PathVariable id: Long): PostDetailResponse {
        return postService.getPost(id)
    }

    @GetMapping("/posts")
    fun getPosts(
        pageable: Pageable,
        postSearchRequest: PostSearchRequest,
    ): Page<PostSummaryResponse> {
        return postService.searchPosts(
            pageable = if (!pageable.isPaged) Pageable.ofSize(10).withPage(0) else pageable,
            request = postSearchRequest.toDto()
        )
    }
}
