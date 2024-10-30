package com.jetbrains.bookmarks

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "bookmarks")
class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookmarks_id_gen")
    @SequenceGenerator(name = "bookmarks_id_gen", sequenceName = "bookmark_id_seq")
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "title", nullable = false, length = 200)
    @field:NotEmpty
    @field:Size(max = 200)
    var title: String? = null

    @Column(name = "url", nullable = false, length = 500)
    @field:NotEmpty
    @field:Size(max = 500)
    var url: String? = null

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    @field:NotNull
    var createdAt: Instant? = null

    @Column(name = "updated_at")
    var updatedAt: Instant? = null
}