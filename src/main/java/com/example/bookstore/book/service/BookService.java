package com.example.bookstore.book.service;

import com.example.bookstore.book.dto.BookResponseDto;
import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.common.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 🔹 A. 전체 목록 조회
    public List<BookResponseDto.BookSummaryResponse> getBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(BookResponseDto.BookSummaryResponse::from)
                .toList();
    }

    // 🔹 B-1. 카테고리별 목록 조회
    public List<BookResponseDto.BookSummaryResponse> getBooksByCategory(Long categoryId) {
        List<Book> books = bookRepository.findByCategoryId(categoryId);

        if (books.isEmpty()) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }

        return books.stream()
                .map(BookResponseDto.BookSummaryResponse::from)
                .toList();
    }

    // 🔹 B-2. 단건 상세 조회
    public BookResponseDto.BookDetailResponse getBookDetail(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));
        return BookResponseDto.BookDetailResponse.from(book);
    }

    // 🔹 C. TOP 10 도서 조회 (평점/리뷰 기준)
    public List<BookResponseDto.BookSummaryResponse> getTopBooks() {
        List<Book> books = bookRepository.findTop10ByOrderByAverageRatingDescReviewCountDesc();
        return books.stream()
                .map(BookResponseDto.BookSummaryResponse::from)
                .toList();
    }

    // 🔹 D. 검색 + 페이지네이션 + 정렬 조회
    public PageResponse<BookResponseDto.BookSummaryResponse> searchBooks(
            String keyword,
            int page,
            int size,
            String sort
    ) {
        // 1) page/size 기본값 & 방어 코드
        if (page < 0) {
            page = 0;
        }
        if (size <= 0 || size > 50) {
            size = 10; // 기본 10개
        }

        // 2) 정렬 기준 설정
        Sort sortSpec;
        if (sort == null || sort.isBlank()) {
            sort = "LATEST";
        }

        switch (sort.toUpperCase()) {
            case "PRICE_ASC" -> sortSpec = Sort.by(Sort.Direction.ASC, "price");
            case "PRICE_DESC" -> sortSpec = Sort.by(Sort.Direction.DESC, "price");
            case "RATING_DESC" ->
                    sortSpec = Sort.by(Sort.Direction.DESC, "averageRating")
                                   .and(Sort.by(Sort.Direction.DESC, "reviewCount"));
            default -> sortSpec = Sort.by(Sort.Direction.DESC, "createdAt"); // LATEST
        }

        Pageable pageable = PageRequest.of(page, size, sortSpec);

        // 3) keyword 유무에 따른 분기
        Page<Book> result;
        if (keyword == null || keyword.isBlank()) {
            // 키워드 없으면 전체 조회 + 페이징
            result = bookRepository.findAll(pageable);
        } else {
            // 키워드 있으면 제목/저자 검색
            result = bookRepository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                            keyword, keyword, pageable
                    );
        }

        // 4) 엔티티 -> DTO 변환
        List<BookResponseDto.BookSummaryResponse> content = result.getContent().stream()
                .map(BookResponseDto.BookSummaryResponse::from)
                .toList();

        // 5) PageResponse 로 감싸서 반환
        return new PageResponse<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast()
        );
    }
}
