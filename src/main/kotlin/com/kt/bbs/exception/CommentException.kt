package com.kt.bbs.exception

open class CommentException(override val message: String) : RuntimeException()

class CommentNotFoundException : CommentException("Comment not found")
class CommentIdMismatchException : CommentException("Comment id mismatch")
class CommentCreatorMismatchException : CommentException("Comment creator mismatch")
