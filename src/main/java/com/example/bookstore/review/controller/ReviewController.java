package com.example.bookstore.review.controller;

import com.example.bookstore.common.response.ApiResponse;
import com.example.bookstore.review.dto.ReviewRequestDto;
import com.example.bookstore.review.dto.ReviewResponseDto;
import com.example.bookstore.review.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 🔹 1. 리뷰 목록 조회
    @GetMapping
    public ApiResponse<List<ReviewResponseDto.ReviewResponse>> getReviews(
            @PathVariable Long bookId
    ) {
        return ApiResponse.success(reviewService.getReviewsByBook(bookId));
    }

    // 🔹 2. 리뷰 작성
    @PostMapping
    public ApiResponse<ReviewResponseDto.ReviewResponse> createReview(
            @PathVariable Long bookId,
            @RequestBody ReviewRequestDto.CreateReviewRequest request
    ) {
        return ApiResponse.success(reviewService.createReview(bookId, request));
    }
}
