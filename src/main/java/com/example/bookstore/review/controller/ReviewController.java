package com.example.bookstore.review.controller;

import com.example.bookstore.common.response.ApiResponse;
import com.example.bookstore.review.dto.ReviewRequestDto;
import com.example.bookstore.review.dto.ReviewResponseDto;
import com.example.bookstore.review.dto.ReviewResponseDto.ReviewResponse;
import com.example.bookstore.review.dto.ReviewUpdateRequest;
import com.example.bookstore.review.service.ReviewService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/reviews")
@Tag(name = "Review API", description = "도서 리뷰 작성/조회/수정/삭제 API")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 🔹 1. 특정 책의 리뷰 목록 조회
    @GetMapping
    @Operation(
            summary = "도서 리뷰 목록 조회",
            description = "특정 도서에 대한 모든 리뷰 목록을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "리뷰 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDto.ReviewResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 도서를 찾을 수 없음"
            )
    })
    public ApiResponse<List<ReviewResponse>> getReviews(
            @Parameter(description = "도서 ID", example = "1")
            @PathVariable Long bookId
    ) {
        List<ReviewResponse> responses = reviewService.getReviewsByBook(bookId);
        return ApiResponse.success(responses);
    }

    // 🔹 2. 리뷰 작성
    @PostMapping
    @Operation(
            summary = "리뷰 작성",
            description = "특정 도서에 대해 새로운 리뷰를 작성합니다. (로그인 필요)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "리뷰 작성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDto.ReviewResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 요청 데이터"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    public ApiResponse<ReviewResponse> createReview(
            @Parameter(description = "리뷰를 작성할 도서 ID", example = "1")
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewRequestDto.CreateReviewRequest request
    ) {
        ReviewResponse response = reviewService.createReview(bookId, request);
        return ApiResponse.success(response);
    }

    // 🔹 3. 리뷰 수정 (본인만)
    @PatchMapping("/{reviewId}")
    @Operation(
            summary = "리뷰 수정",
            description = "본인이 작성한 리뷰 내용을 수정합니다. (로그인 필요)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "리뷰 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewResponseDto.ReviewResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "다른 사용자의 리뷰를 수정하려는 경우"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 리뷰 또는 도서를 찾을 수 없음"
            )
    })
    public ApiResponse<ReviewResponse> updateReview(
            @Parameter(description = "도서 ID", example = "1")
            @PathVariable Long bookId,
            @Parameter(description = "수정할 리뷰 ID", example = "10")
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequest request
    ) {
        ReviewResponse response = reviewService.updateReview(bookId, reviewId, request);
        return ApiResponse.success(response);
    }

    // 🔹 4. 리뷰 삭제 (본인만)
    @DeleteMapping("/{reviewId}")
    @Operation(
            summary = "리뷰 삭제",
            description = "본인이 작성한 리뷰를 삭제합니다. (로그인 필요)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "리뷰 삭제 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "다른 사용자의 리뷰를 삭제하려는 경우"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 리뷰 또는 도서를 찾을 수 없음"
            )
    })
    public ApiResponse<Void> deleteReview(
            @Parameter(description = "도서 ID", example = "1")
            @PathVariable Long bookId,
            @Parameter(description = "삭제할 리뷰 ID", example = "10")
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(bookId, reviewId);
        return ApiResponse.success(null);
    }
}
