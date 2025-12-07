package com.example.bookstore.book.controller;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books Query API", description = "도서 추가 조회(최신/평점순) API")
public class BookQueryController {

    private final BookRepository bookRepository;

    public BookQueryController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * 최근 등록된 도서 목록
     * 예) GET /api/books/latest?size=10
     */
    @GetMapping("/latest")
    @Operation(
            summary = "최근 등록 도서 목록",
            description = "등록일(createdAt) 기준으로 최신 도서를 size 개수만큼 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "최근 도서 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Book.class)
                    )
            )
    })
    public ApiResponse<List<Book>> getLatestBooks(
            @Parameter(description = "조회할 도서 개수", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        var pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Book> books = bookRepository.findAll(pageable).getContent();
        return ApiResponse.success(books);
    }

    /**
     * 평점 상위 도서 목록
     * 예) GET /api/books/top-rated?size=10
     */
    @GetMapping("/top-rated")
    @Operation(
            summary = "평점 상위 도서 목록",
            description = "평균 평점(averageRating)과 리뷰 수(reviewCount)를 기준으로 상위 도서를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "평점 상위 도서 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Book.class)
                    )
            )
    })
    public ApiResponse<List<Book>> getTopRatedBooks(
            @Parameter(description = "조회할 도서 개수", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        // averageRating, reviewCount 기준으로 정렬
        var sort = Sort.by(Sort.Direction.DESC, "averageRating")
                .and(Sort.by(Sort.Direction.DESC, "reviewCount"));
        var pageable = PageRequest.of(0, size, sort);
        List<Book> books = bookRepository.findAll(pageable).getContent();
        return ApiResponse.success(books);
    }
}
