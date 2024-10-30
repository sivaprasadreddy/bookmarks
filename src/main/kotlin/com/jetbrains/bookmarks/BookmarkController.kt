package com.jetbrains.bookmarks

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.time.Instant
import java.util.function.Supplier

@RestController
@RequestMapping("/api/bookmarks")
internal class BookmarkController(private val bookmarkRepository: BookmarkRepository) {

    @GetMapping
    fun bookmarks() = bookmarkRepository.findAllByOrderByCreatedAtDesc()

    @GetMapping("/{id}")
    fun getBookmarkById(@PathVariable id: Long): ResponseEntity<BookmarkInfo> {
        val bookmark = bookmarkRepository.findBookmarkById(id)
            ?: throw BookmarkNotFoundException("Bookmark not found")
        return ResponseEntity.ok(bookmark)
    }

    internal data class CreateBookmarkPayload(
        @field:NotEmpty(message = "Title is required") val title:  String,
        @field:NotEmpty(message = "Url is required") val url:  String
    )

    @PostMapping
    fun createBookmark(@RequestBody @Valid payload: CreateBookmarkPayload): ResponseEntity<Void> {
        val bookmark = Bookmark().apply {
            title = payload.title
            url = payload.url
            createdAt = Instant.now()
        }
        bookmarkRepository.save(bookmark)
        val url = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .build(bookmark.id)
        return ResponseEntity.created(url).build()
    }

    internal data class UpdateBookmarkPayload(
        @field:NotEmpty(message = "Title is required") val title:  String,
        @field:NotEmpty(message = "Url is required") val url:  String
    )

    @PutMapping("/{id}")
    fun updateBookmark(
        @PathVariable id: Long,
        @RequestBody @Valid payload: UpdateBookmarkPayload
    ): ResponseEntity<Void> {
        val bookmark = bookmarkRepository.findById(id)
                .orElseThrow { BookmarkNotFoundException("Bookmark not found") }

        with(bookmark) {
            title = payload.title
            url = payload.url
            updatedAt = Instant.now()
        }

        bookmarkRepository.save(bookmark)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteBookmark(@PathVariable id: Long) {
        val bookmark = bookmarkRepository.findById(id)
            .orElseThrow { BookmarkNotFoundException("Bookmark not found") }
        bookmarkRepository.delete(bookmark)
    }

    @ExceptionHandler(BookmarkNotFoundException::class)
    fun handle(e: BookmarkNotFoundException?): ResponseEntity<Void> {
        return ResponseEntity.notFound().build()
    }
}
