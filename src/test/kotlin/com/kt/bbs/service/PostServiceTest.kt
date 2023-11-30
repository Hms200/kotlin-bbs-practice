package com.kt.bbs.service

import com.kt.bbs.exception.PostCouldNotBeDeletedException
import com.kt.bbs.exception.PostCreatorMismatchException
import com.kt.bbs.exception.PostNotFoundException
import com.kt.bbs.repository.PostRepository
import com.kt.bbs.service.dto.PostCreateRequestDto
import com.kt.bbs.service.dto.PostUpdateRequestDto
import com.kt.bbs.service.dto.toEntity
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    given("게시글 생성") {

        When("게시글 생성 요청") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "title",
                    content = "content",
                    createdBy = "createdBy"
                )
            )

            then("게시글이 생성된다") {
                postId shouldBeGreaterThan 0
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "title"
                post?.content shouldBe "content"
                post?.createdBy shouldBe "createdBy"
            }
        }
    }

    given("게시글 수정") {
        val post = postRepository.save(
            PostCreateRequestDto(
                title = "title",
                content = "content",
                createdBy = "createdBy"
            ).toEntity()
        )

        When("정상 수정 요청") {
            postService.updatePost(
                post.id, PostUpdateRequestDto(
                    title = "title2",
                    content = "content2",
                    updatedBy = "createdBy"
                )
            )

            then("게시글이 수정된다") {
                val updatedPost = postRepository.findByIdOrNull(post.id)
                updatedPost shouldNotBe null
                updatedPost?.id shouldBe post.id
                updatedPost?.title shouldBe "title2"
                updatedPost?.content shouldBe "content2"
                updatedPost?.updatedBy shouldBe "createdBy"
            }
        }

        When("없는 게시글 일 때") {

            then("예외 발생") {
                shouldThrowExactly<PostNotFoundException> {
                    postService.updatePost(
                        100L, PostUpdateRequestDto(
                            title = "title2",
                            content = "content2",
                            updatedBy = "updatedBy"
                        )
                    )
                }
            }
        }

        When("작성자가 동일하지 않을 때") {

            then("작성자 다름 예외 발생") {
                shouldThrowExactly<PostCreatorMismatchException> {
                    postService.updatePost(
                        1L, PostUpdateRequestDto(
                            title = "title2",
                            content = "content2",
                            updatedBy = "updatedBy"
                        )
                    )
                }
            }
        }
    }

    given("게시글 삭제시") {

        val post = postRepository.save(
            PostCreateRequestDto(
                title = "title",
                content = "content",
                createdBy = "createdBy"
            ).toEntity()
        )

        When("정상 삭제") {

            then("게시글이 삭제된다.") {
                postService.deletePost(post.id, "createdBy")
            }
        }

        When("작성자가 다르면") {

            then("예외 발생") {
                shouldThrowExactly<PostCouldNotBeDeletedException> {
                    postService.deletePost(post.id, "updatedBy")
                }
            }
        }
    }
})
