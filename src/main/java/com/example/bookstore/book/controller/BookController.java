package com.example.bookstore.book.controller;
import com.example.bookstore.common.response.PageResponse;

import com.example.bookstore.book.dto.BookResponseDto;
import com.example.bookstore.book.service.BookService;
import com.example.bookstore.common.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 🔹 A. 전체 조회
    @GetMapping
    public ApiResponse<List<BookResponseDto.BookSummaryResponse>> getAllBooks() {
        return ApiResponse.success(bookService.getBooks());
    }

    // 🔹 B. 카테고리별 조회
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<BookResponseDto.BookSummaryResponse>> getByCategory(@PathVariable Long categoryId) {
        return ApiResponse.success(bookService.getBooksByCategory(categoryId));
    }

    // 🔹 C. 상세 조회
    @GetMapping("/{bookId}")
    public ApiResponse<BookResponseDto.BookDetailResponse> getBook(@PathVariable Long bookId) {
        return ApiResponse.success(bookService.getBookDetail(bookId));
    }

    // 🔹 D. TOP 10 도서 조회
    @GetMapping("/top")
    public ApiResponse<List<BookResponseDto.BookSummaryResponse>> getTopBooks() {
        return ApiResponse.success(bookService.getTopBooks());
    }

    // 클래스 안에 메서드 추가
    @GetMapping("/search")
    public ApiResponse<PageResponse<BookResponseDto.BookSummaryResponse>> searchBooks(
        @RequestParam(required = false, defaultValue = "") String keyword,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "LATEST") String sort) {
        return ApiResponse.success(bookService.searchBooks(keyword, page, size, sort));
    }

}
