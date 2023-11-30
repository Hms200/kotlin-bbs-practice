package com.kt.bbs.service

import com.kt.bbs.exception.PostCouldNotBeDeletedException
import com.kt.bbs.exception.PostNotFoundException
import com.kt.bbs.repository.PostRepository
import com.kt.bbs.service.dto.PostCreateRequestDto
import com.kt.bbs.service.dto.PostUpdateRequestDto
import com.kt.bbs.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
) {

    @Transactional
    fun createPost(requestDto: PostCreateRequestDto): Long {
        return postRepository.save(requestDto.toEntity()).id
    }

    @Transactional
    fun updatePost(id: Long, requestDto: PostUpdateRequestDto): Long {
        val post = postRepository.findByIdOrNull(id)
            ?: throw PostNotFoundException()
        post.update(requestDto)
        return id
    }

    @Transactional
    fun deletePost(id: Long, createdBy: String): Long {
        val post = postRepository.findByIdOrNull(id)
            ?: throw PostNotFoundException()
        if (post.createdBy != createdBy) throw PostCouldNotBeDeletedException()
        postRepository.delete(post)
        return id
    }

}
