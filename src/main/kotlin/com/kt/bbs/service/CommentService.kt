package com.kt.bbs.service

import com.kt.bbs.exception.CommentCreatorMismatchException
import com.kt.bbs.exception.CommentNotFoundException
import com.kt.bbs.exception.PostNotFoundException
import com.kt.bbs.repository.CommentRepository
import com.kt.bbs.repository.PostRepository
import com.kt.bbs.service.dto.CommentCreateRequestDto
import com.kt.bbs.service.dto.CommentUpdateRequestDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {

    @Transactional
    fun createComment(commentCreateRequestDto: CommentCreateRequestDto): Long {
        val post = postRepository.findByIdOrNull(commentCreateRequestDto.postId)
            ?: throw PostNotFoundException()

        return commentRepository.save(commentCreateRequestDto.toEntity(post)).id
    }

    @Transactional
    fun updateComment(request: CommentUpdateRequestDto): Long {
        val comment = commentRepository.findByIdOrNull(request.commentId)
            ?: throw CommentNotFoundException()

        comment.update(request)

        return comment.id
    }

    @Transactional
    fun deleteComment(commentId: Long, deletedBy: String): Long {
        val comment = commentRepository.findByIdOrNull(commentId)
            ?: throw CommentNotFoundException()

        if (comment.createdBy != deletedBy) throw CommentCreatorMismatchException()

        commentRepository.delete(comment)

        return commentId
    }

}
