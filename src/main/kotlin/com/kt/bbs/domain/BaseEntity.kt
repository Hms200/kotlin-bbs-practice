package com.kt.bbs.domain

import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity(
    val createdBy: String,
) {
    val createdAt: LocalDateTime = LocalDateTime.now()
    var updatedBy: String? = null
        protected set
    var updatedAt: LocalDateTime? = null
        protected set

    fun update(updatedBy: String) {
        this.updatedBy = updatedBy
        this.updatedAt = LocalDateTime.now()
    }
}
