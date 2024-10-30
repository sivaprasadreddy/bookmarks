package com.jetbrains.bookmarks

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.http.HttpStatus
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

class CreateBookmarkPayload(
    @field:NotEmpty(message = "Title is required") val title: String,
    @field:NotEmpty(message = "Url is required") val url: String
)

class UpdateBookmarkPayload(
    @field:NotEmpty(message = "Title is required") val title: String,
    @field:NotEmpty(message = "Url is required") val url: String
)

@RestController
@RequestMapping("/api/bookmarks")
class BookmarkController(private val bookmarkRepository: BookmarkRepository) {

    @GetMapping
    fun bookmarks() = bookmarkRepository.findAllByOrderByCreatedAtDesc()

    @GetMapping("/{id}")
    fun getBookmarkById(@PathVariable id: Long): ResponseEntity<BookmarkInfo> =
        findBookmarkById(id)?.let { ResponseEntity.ok(it) }
            ?: throw BookmarkNotFoundException("Bookmark not found with id: $id")

    @PostMapping
    fun createBookmark(@RequestBody @Valid payload: CreateBookmarkPayload): ResponseEntity<Void> {
        val savedBookmark = bookmarkRepository.save(Bookmark().apply {
            title = payload.title
            url = payload.url
            createdAt = Instant.now()
        })

        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedBookmark.id)
            .toUri()

        return ResponseEntity.created(location).build()
    }

    @PutMapping("/{id}")
    fun updateBookmark(
        @PathVariable id: Long,
        @RequestBody @Valid payload: UpdateBookmarkPayload
    ): ResponseEntity<Void> {
        val bookmark = findBookmarkOrThrow(id)

        bookmark.apply {
            title = payload.title
            url = payload.url
            updatedAt = Instant.now()
        }

        bookmarkRepository.save(bookmark)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteBookmark(@PathVariable id: Long) {
        val bookmark = findBookmarkOrThrow(id)
        bookmarkRepository.delete(bookmark)
    }

    @ExceptionHandler(BookmarkNotFoundException::class)
    fun handleBookmarkNotFound(e: BookmarkNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message))

    private fun findBookmarkById(id: Long): BookmarkInfo? = bookmarkRepository.findBookmarkById(id)

    private fun findBookmarkOrThrow(id: Long): Bookmark =
        bookmarkRepository.findById(id).orElseThrow {
            BookmarkNotFoundException("Bookmark not found with id: $id")
        }
}

data class ErrorResponse(val message: String?)
