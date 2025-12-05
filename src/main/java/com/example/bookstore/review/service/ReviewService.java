package com.example.bookstore.review.service;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.review.dto.ReviewRequestDto;
import com.example.bookstore.review.dto.ReviewResponseDto;
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
    public List<ReviewResponseDto.ReviewResponse> getReviewsByBook(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
        return reviews.stream()
                .map(ReviewResponseDto.ReviewResponse::from)
                .toList();
    }

    // 🔹 2. 리뷰 작성 + Book 평점/리뷰수 갱신
    public ReviewResponseDto.ReviewResponse createReview(
            Long bookId,
            ReviewRequestDto.CreateReviewRequest request
    ) {
        // 1) 책 존재 여부 확인
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        // 2) [임시] 유저 ID 설정
        //    - 지금은 인증/로그인 연동 전이므로 하드코딩
        //    - 나중에 JWT/시큐리티 연결되면 여기서 현재 로그인 유저 ID를 가져와서 사용
        Long userId = 1L;

        // 3) 리뷰 저장
        Review review = Review.builder()
                .userId(userId)                 // 🔹 user_id 컬럼 채우기
                .bookId(bookId)
                .rating(request.getRating())
                .content(request.getContent())
                .build();

        reviewRepository.save(review);

        // 4) 해당 책의 모든 리뷰를 다시 조회해서 평균/갯수 재계산
        List<Review> reviews = reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
        long reviewCount = reviews.size();
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        // 5) Book 엔티티에 반영
        book.setReviewCount(reviewCount);
        book.setAverageRating(averageRating);
        bookRepository.save(book);

        // 6) 방금 저장한 리뷰를 DTO로 변환해서 반환
        return ReviewResponseDto.ReviewResponse.from(review);
    }
}
