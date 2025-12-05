package com.example.bookstore.book.dto;

import com.example.bookstore.book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class BookResponseDto {

    // 🔹 목록용 DTO (요약 정보)
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookSummaryResponse {
        private Long id;
        private String title;
        private String author;
        private int price;
        private double averageRating;
        private long reviewCount;

        public static BookSummaryResponse from(Book book) {
            return BookSummaryResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .price(book.getPrice())
                    .averageRating(book.getAverageRating())
                    .reviewCount(book.getReviewCount())
                    .build();
        }
    }

    // 🔹 상세 조회용 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookDetailResponse {
        private Long id;
        private String title;
        private String author;
        private int price;
        private int stock;
        private double averageRating;
        private long reviewCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static BookDetailResponse from(Book book) {
            return BookDetailResponse.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .author(book.getAuthor())
                    .price(book.getPrice())
                    .stock(book.getStock())
                    .averageRating(book.getAverageRating())
                    .reviewCount(book.getReviewCount())
                    .createdAt(book.getCreatedAt())
                    .updatedAt(book.getUpdatedAt())
                    .build();
        }
    }
}
