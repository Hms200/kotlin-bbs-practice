package com.kt.bbs.service

import com.kt.bbs.domain.Comment
import com.kt.bbs.domain.Post
import com.kt.bbs.exception.CommentCreatorMismatchException
import com.kt.bbs.exception.CommentNotFoundException
import com.kt.bbs.exception.PostNotFoundException
import com.kt.bbs.repository.CommentRepository
import com.kt.bbs.repository.PostRepository
import com.kt.bbs.service.dto.CommentCreateRequestDto
import com.kt.bbs.service.dto.CommentUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class CommentServiceTest(
    private val commentService: CommentService,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    val post = postRepository.save(
        Post(
            title = "제목",
            content = "내용",
            createdBy = "작성자",
        )
    )

    val comment = commentRepository.save(
        Comment(
            content = "댓글 내용",
            post = post,
            createdBy = "댓글 작성자",
        )
    )

    Given("create comment") {

        When("정상적인 요청인 경우") {
            val request = CommentCreateRequestDto(
                postId = 1L,
                content = "댓글 내용",
                createdBy = "댓글 작성자",
            )
            then("댓글이 생성된다.") {
                val commentId = commentService.createComment(request)
                val comment = commentRepository.findByIdOrNull(commentId)

                commentId shouldBeGreaterThan 0L
                comment shouldNotBe null
                comment?.content shouldBe request.content
                comment?.createdBy shouldBe request.createdBy
            }
        }

        When("존재하지 않는 게시글에 댓글을 생성하는 경우") {
            val request = CommentCreateRequestDto(
                postId = 2L,
                content = "댓글 내용",
                createdBy = "댓글 작성자",
            )

            then("예외가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    commentService.createComment(request)
                }
            }
        }

    }

    Given("update Comment") {


        When("정상 요청인 경우") {

            then("댓글 내용이 수정됨") {
                val request = CommentUpdateRequestDto(
                    commentId = comment.id,
                    content = "수정된 댓글 내용",
                    updatedBy = "댓글 작성자",
                )

                val result = commentService.updateComment(request)
                val updated = commentRepository.findByIdOrNull(result)

                result shouldBe comment.id
                updated shouldNotBe null
                updated?.content shouldBe request.content
            }
        }

        When("작성자가 동일하지 않은 경우") {

            then("예외 발생") {
                val request = CommentUpdateRequestDto(
                    commentId = comment.id,
                    content = "수정된 댓글 내용",
                    updatedBy = "댓글 작성자가 아님",
                )

                shouldThrowExactly<CommentCreatorMismatchException> {
                    commentService.updateComment(request)
                }
            }
        }

        When("존재하지 않는 댓글인 경우") {

            then("에외 발생") {
                val request = CommentUpdateRequestDto(
                    commentId = comment.id + 1,
                    content = "수정된 댓글 내용",
                    updatedBy = "댓글 작성자",
                )

                shouldThrowExactly<CommentNotFoundException> {
                    commentService.updateComment(request)
                }
            }
        }
    }

    Given("delete comment") {

        When("정상 요청인 경우") {

            then("정상 삭제됨") {
                val result = commentService.deleteComment(comment.id, comment.createdBy)

                result shouldBe comment.id

                commentRepository.findByIdOrNull(comment.id) shouldBe null
            }
        }

        When("작성자가 다른경우") {

            then("예외 발생") {
                shouldThrowExactly<CommentCreatorMismatchException> {
                    commentService.deleteComment(comment.id, "댓글 작성자가 아님")
                }
            }
        }
    }
    
})
