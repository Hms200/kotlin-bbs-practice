package com.kt.bbs.controller

import com.kt.bbs.controller.dto.CommentCreateRequest
import com.kt.bbs.controller.dto.CommentUpdateRequest
import com.kt.bbs.service.CommentService
import com.kt.bbs.service.dto.toDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController(
    private val commentService: CommentService,
) {

    @PostMapping("/posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): Long {
        return commentService.createComment(
            commentCreateRequest.toDto(
                postId = postId,
                request = commentCreateRequest
            )
        )
    }

    @PutMapping("/comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): Long {
        return commentService.updateComment(
            commentUpdateRequest.toDto(commentId)
        )
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @RequestParam deletedBy: String,
    ): Long {
        return commentService.deleteComment(
            commentId = commentId,
            deletedBy = deletedBy,
        )
    }
}
