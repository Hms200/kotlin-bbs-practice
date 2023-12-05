package com.kt.bbs.service

import com.kt.bbs.controller.dto.PostSearchRequest
import com.kt.bbs.exception.PostCouldNotBeDeletedException
import com.kt.bbs.exception.PostCreatorMismatchException
import com.kt.bbs.exception.PostNotFoundException
import com.kt.bbs.repository.PostRepository
import com.kt.bbs.service.dto.CommentCreateRequestDto
import com.kt.bbs.service.dto.PostCreateRequestDto
import com.kt.bbs.service.dto.PostUpdateRequestDto
import com.kt.bbs.service.dto.toDto
import com.kt.bbs.service.dto.toEntity
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val commentService: CommentService,
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
                post.id,
                PostUpdateRequestDto(
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
                        100L,
                        PostUpdateRequestDto(
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
                        1L,
                        PostUpdateRequestDto(
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

    given("게시글 조회") {
        postRepository.saveAll(
            listOf(
                PostCreateRequestDto(
                    title = "title",
                    content = "content",
                    createdBy = "createdBy"
                ).toEntity(),
                PostCreateRequestDto(
                    title = "tite2",
                    content = "content2",
                    createdBy = "createdBy2"
                ).toEntity(),
                PostCreateRequestDto(
                    title = "title",
                    content = "content3",
                    createdBy = "createdBy"
                ).toEntity()
            )
        )

        When("존재하는 게시글 단건 조회") {
            then("정상 조회된다.") {
                val postDetail = postService.getPost(1L)
                postDetail shouldNotBe null
                postDetail.id shouldBe 1L
            }
        }

        When("id가 존재하지 않는 게시글 조회") {
            then("예외 발생") {
                shouldThrowExactly<PostNotFoundException> {
                    postService.getPost(100L)
                }
            }
        }

        When("제목 또는 작성자로 목록 검색 요청") {
            val titleOnly = PostSearchRequest(
                title = "title",
                createdBy = null
            ).toDto()
            val createdByOnly = PostSearchRequest(
                title = null,
                createdBy = "createdBy2"
            ).toDto()
            val titleAndCreatedBy = PostSearchRequest(
                title = "title",
                createdBy = "createdBy"
            ).toDto()
            val page = Pageable.ofSize(10).withPage(0)
            then("게시글 목록을 반환한다. order by createdAt desc") {
                val result1 = postService.searchPosts(page, titleOnly)
                result1.content shouldNotBe null
                result1.totalElements shouldBe 2
                result1.content.all { it.title == "title" } shouldBe true
                LocalDateTime.parse(result1.content[0].createdAt).isAfter(
                    (LocalDateTime.parse(result1.content[1].createdAt))
                ) shouldBe true

                val result2 = postService.searchPosts(page, createdByOnly)
                result2.content shouldNotBe null
                result2.totalElements shouldBe 1
                result2.content.all { it.createdBy == "createdBy2" } shouldBe true

                val result3 = postService.searchPosts(page, titleAndCreatedBy)
                result3.content shouldNotBe null
                result3.totalElements shouldBe 2
                result3.content.all { it.title == "title" && it.createdBy == "createdBy" } shouldBe true
                LocalDateTime.parse(result3.content[0].createdAt).isAfter(
                    (LocalDateTime.parse(result3.content[1].createdAt))
                ) shouldBe true
            }
        }

        When("댓글이 있는 경우") {
            commentService.createComment(
                CommentCreateRequestDto(
                    postId = 1L,
                    content = "댓글 내용",
                    createdBy = "댓글 작성자",
                )
            )
            commentService.createComment(
                CommentCreateRequestDto(
                    postId = 1L,
                    content = "댓글 내용2",
                    createdBy = "댓글 작성자2",
                )
            )

            then("댓글 목록도 리턴") {
                val result = postService.getPost(1L)

                result.comments shouldNotBe null
                result.comments.size shouldBe 2
                result.comments[0].content shouldBe "댓글 내용2"
                result.comments[1].content shouldBe "댓글 내용"
                result.comments[0].createdBy shouldBe "댓글 작성자2"
                result.comments[1].createdBy shouldBe "댓글 작성자"
            }
        }

    }
})
