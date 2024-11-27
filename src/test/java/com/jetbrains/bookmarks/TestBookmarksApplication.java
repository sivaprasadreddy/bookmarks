package com.jetbrains.bookmarks;

import org.springframework.boot.SpringApplication;

public class TestBookmarksApplication {
    public static void main(String[] args) {
        SpringApplication
                .from(BookmarksApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
