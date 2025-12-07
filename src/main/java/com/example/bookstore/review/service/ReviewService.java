package com.example.bookstore.review.service;

import com.example.bookstore.auth.jwt.SecurityUtil;
import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.review.dto.ReviewRequestDto;
import com.example.bookstore.review.dto.ReviewResponseDto;
import com.example.bookstore.review.dto.ReviewResponseDto.ReviewResponse;
import com.example.bookstore.review.dto.ReviewUpdateRequest;
import com.example.bookstore.review.entity.Review;
import com.example.bookstore.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    // 🔹 1. 특정 책의 리뷰 목록 조회
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByBook(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
        return reviews.stream()
                .map(ReviewResponse::from)
                .toList();
    }

    // 🔹 2. 리뷰 작성 + Book 평점/리뷰수 갱신
    public ReviewResponse createReview(
            Long bookId,
            ReviewRequestDto.CreateReviewRequest request
    ) {
        // 1) 책 존재 여부 확인
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        // 2) 현재 로그인한 사용자 ID (JWT 기반)
        Long userId = SecurityUtil.getCurrentUserIdOrThrow();

        // 3) 리뷰 저장
        Review review = Review.builder()
                .userId(userId)
                .bookId(bookId)
                .rating(request.getRating())
                .content(request.getContent())
                .build();

        reviewRepository.save(review);

        // 4) 책의 평균 평점/리뷰수 갱신
        recalcBookStats(book);

        // 5) DTO 변환
        return ReviewResponse.from(review);
    }

    // 🔹 3. 리뷰 수정 (본인만 + 책 검증 + Book 평점/리뷰수 갱신)
    public ReviewResponse updateReview(Long bookId,
                                       Long reviewId,
                                       ReviewUpdateRequest request) {

        Long currentUserId = SecurityUtil.getCurrentUserIdOrThrow();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        // bookId 일치 여부 확인
        if (!review.getBookId().equals(bookId)) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }

        // 작성자 본인인지 확인
        if (!review.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 내용 수정
        review.setRating(request.getRating());
        review.setContent(request.getContent());

        // 책 평점/리뷰수 갱신
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));
        recalcBookStats(book);

        return ReviewResponse.from(review);
    }

    // 🔹 4. 리뷰 삭제 (본인만 + Book 평점/리뷰수 갱신)
    public void deleteReview(Long bookId, Long reviewId) {

        Long currentUserId = SecurityUtil.getCurrentUserIdOrThrow();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!review.getBookId().equals(bookId)) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }

        if (!review.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        reviewRepository.delete(review);

        // 책 평점/리뷰수 갱신
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));
        recalcBookStats(book);
    }

    // 🔹 공통: 책의 평균 평점 및 리뷰 수 다시 계산
    private void recalcBookStats(Book book) {
        List<Review> reviews = reviewRepository.findByBookIdOrderByCreatedAtDesc(book.getId());
        long reviewCount = reviews.size();
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        book.setReviewCount(reviewCount);
        book.setAverageRating(averageRating);
        bookRepository.save(book);
    }
}
