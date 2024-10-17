package com.jetbrains.bookmarks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
  List<BookmarkInfo> findAllByOrderByCreatedAtDesc();

  Optional<BookmarkInfo> findBookmarkById(Long id);
}
