package com.kt.bbs.repository

import com.kt.bbs.domain.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long>
