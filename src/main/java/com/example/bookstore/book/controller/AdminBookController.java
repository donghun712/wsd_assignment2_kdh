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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/books")
@Tag(name = "Admin Book API", description = "관리자용 도서 관리/조회 API")
public class AdminBookController {

    private final BookRepository bookRepository;

    public AdminBookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * 관리자용 도서 목록 조회 (페이지네이션)
     * 예) GET /api/admin/books?page=0&size=20&sort=id,desc
     */
    @GetMapping
    @Operation(
            summary = "도서 목록 조회(관리자)",
            description = "관리자가 모든 도서 목록을 페이지네이션 형태로 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "도서 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "관리자 권한이 없는 경우 접근 불가"
            )
    })
    public ApiResponse<Page<Book>> getBooks(
            @Parameter(hidden = true) Pageable pageable
    ) {
        Page<Book> page = bookRepository.findAll(pageable);
        return ApiResponse.success(page);
    }
}
