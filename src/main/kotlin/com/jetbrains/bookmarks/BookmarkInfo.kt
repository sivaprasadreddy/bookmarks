package com.jetbrains.bookmarks

import java.time.Instant

/**
 * Projection for [Bookmark]
 */
interface BookmarkInfo {
    val id: Long

    val title: String

    val url: String

    val createdAt: Instant
}