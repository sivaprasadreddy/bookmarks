package com.jetbrains.bookmarks

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookmarkRepository : JpaRepository<Bookmark, Long> {
    fun findAllByOrderByCreatedAtDesc(): List<BookmarkInfo>

    fun findBookmarkById(id: Long): BookmarkInfo?
}
