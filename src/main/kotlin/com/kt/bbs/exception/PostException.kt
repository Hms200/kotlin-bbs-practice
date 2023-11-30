package com.kt.bbs.exception

open class PostException(message: String) : RuntimeException(message)

class PostNotFoundException : PostException("게시글을 찾을 수 없습니다.")
class PostCreatorMismatchException : PostException("게시글 작성자만 수정할 수 있습니다.")
class PostCouldNotBeDeletedException : PostException("게시글 작성자만 삭제할 수 없습니다.")
