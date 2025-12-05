package com.example.bookstore.review.dto;

import com.example.bookstore.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ReviewResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewResponse {
        private Long id;
        private Long userId;     // 🔹 추가
        private Long bookId;
        private int rating;
        private String content;
        private LocalDateTime createdAt;

        public static ReviewResponse from(Review review) {
            return ReviewResponse.builder()
                    .id(review.getId())
                    .userId(review.getUserId())      // 🔹 추가
                    .bookId(review.getBookId())
                    .rating(review.getRating())
                    .content(review.getContent())
                    .createdAt(review.getCreatedAt())
                    .build();
        }
    }
}
