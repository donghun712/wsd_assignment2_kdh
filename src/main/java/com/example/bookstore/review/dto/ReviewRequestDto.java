package com.example.bookstore.review.dto;

import lombok.Getter;
import lombok.Setter;

public class ReviewRequestDto {

    @Getter
    @Setter
    public static class CreateReviewRequest {
        private int rating;
        private String content;
        // reviewerName 제거
    }
}
