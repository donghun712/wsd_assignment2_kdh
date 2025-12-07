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
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books API", description = "도서 조회 / 카테고리 / 상세 / 추천 / 검색 API")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * 전체 도서 목록 조회
     */
    @GetMapping
    @Operation(
            summary = "전체 도서 조회",
            description = "등록된 모든 도서 목록을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "도서 목록 조회 성공",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ApiResponse<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ApiResponse.success(books);
    }

    /**
     * 카테고리별 도서 목록 조회
     */
    @GetMapping("/category/{categoryId}")
    @Operation(
            summary = "카테고리별 도서 조회",
            description = "특정 카테고리에 속한 도서 목록을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "카테고리별 도서 목록 조회 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 카테고리 ID 요청"
            )
    })
    public ApiResponse<List<Book>> getByCategory(
            @Parameter(description = "카테고리 ID", example = "1")
            @PathVariable Long categoryId
    ) {
        List<Book> books = bookRepository.findByCategoryId(categoryId);
        return ApiResponse.success(books);
    }

    /**
     * 단일 도서 상세 조회
     */
    @GetMapping("/{bookId}")
    @Operation(
            summary = "도서 상세 조회",
            description = "도서 ID로 단일 도서 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "도서 상세 조회 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 ID의 도서를 찾을 수 없음"
            )
    })
    public ApiResponse<Book> getBook(
            @Parameter(description = "도서 ID", example = "1")
            @PathVariable Long bookId
    ) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다. id = " + bookId));
        return ApiResponse.success(book);
    }

    /**
     * 추천 도서 목록 조회
     * - 주문 기반 베스트셀러
     * - 평점/리뷰 수 기준 상위 도서
     */
    @GetMapping("/recommendations")
    @Operation(
            summary = "추천 도서 조회",
            description = """
                    주문 수 기준 베스트셀러 목록과
                    평점/리뷰 수 기준 상위 도서를 함께 반환합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "추천 도서 목록 조회 성공",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ApiResponse<BookRecommendationResponse> getRecommendations(
            @Parameter(description = "가져올 도서 수 (기본 10)", example = "10")
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        if (size <= 0) size = 10;

        // (1) 주문 기반 베스트셀러
        List<Book> bestSellers = bookRepository.findBestSellerBooks(size);

        // (2) 평점/리뷰 기반 TOP 도서
        List<Book> topRated = bookRepository.findTop10ByOrderByAverageRatingDescReviewCountDesc();

        BookRecommendationResponse response = BookRecommendationResponse.of(bestSellers, topRated);
        return ApiResponse.success(response);
    }

    /**
     * 도서 검색 + 페이지네이션 + 정렬
     *
     * 예시:
     *  - /api/books/search?keyword=자바&page=0&size=10&sort=createdAt,DESC
     *  - /api/books/search?categoryId=1&sort=price,ASC
     */
    @GetMapping("/search")
    @Operation(
            summary = "도서 검색",
            description = """
                    키워드, 카테고리, 페이지, 정렬 조건을 이용해 도서 목록을 검색합니다.
                    sort 파라미터는 "정렬필드,정렬방향" 형식으로 전달합니다. (예: createdAt,DESC)
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "검색 조건에 따른 도서 목록 조회 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 페이지/정렬 파라미터"
            )
    })
    public ApiResponse<PageResponse<BookSummary>> searchBooks(
            @Parameter(description = "검색 키워드(제목, 저자 등 부분 일치)", example = "자바")
            @RequestParam(name = "keyword", required = false) String keyword,

            @Parameter(description = "카테고리 ID 필터", example = "1")
            @RequestParam(name = "categoryId", required = false) Long categoryId,

            @Parameter(description = "페이지 번호(0부터 시작)", example = "0")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(name = "size", defaultValue = "10") int size,

            @Parameter(description = "정렬 조건 (예: createdAt,DESC / price,ASC)", example = "createdAt,DESC")
            @RequestParam(name = "sort", defaultValue = "createdAt,DESC") String sortParam
    ) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 50) size = 50; // 최대 페이지 크기 제한 (성능용)

        // sort 파라미터 파싱: "createdAt,DESC" 형식
        String property = "createdAt";
        Sort.Direction direction = Sort.Direction.DESC;

        if (sortParam != null && !sortParam.isBlank()) {
            String[] parts = sortParam.split(",");
            if (parts.length >= 1 && !parts[0].isBlank()) {
                property = parts[0];
            }
            if (parts.length >= 2) {
                if ("ASC".equalsIgnoreCase(parts[1])) {
                    direction = Sort.Direction.ASC;
                } else if ("DESC".equalsIgnoreCase(parts[1])) {
                    direction = Sort.Direction.DESC;
                }
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, property));

        Page<Book> resultPage = bookRepository.searchBooks(keyword, categoryId, pageable);

        PageResponse<BookSummary> response = PageResponse.from(
                resultPage.map(BookSummary::from),
                property + "," + direction.name()
        );

        return ApiResponse.success(response);
    }

    // ====== DTO: 추천 응답 ======
    @Schema(name = "BookRecommendationResponse", description = "추천 도서 응답 DTO")
    public static class BookRecommendationResponse {

        @Schema(description = "주문 수 기준 베스트셀러 목록")
        private List<BookSummary> bestSellers;

        @Schema(description = "평점/리뷰 수 기준 상위 도서 목록")
        private List<BookSummary> topRated;

        public BookRecommendationResponse(List<BookSummary> bestSellers, List<BookSummary> topRated) {
            this.bestSellers = bestSellers;
            this.topRated = topRated;
        }

        public static BookRecommendationResponse of(List<Book> bestSellers, List<Book> topRated) {
            List<BookSummary> bestSellerDtos = bestSellers.stream()
                    .map(BookSummary::from)
                    .toList();

            List<BookSummary> topRatedDtos = topRated.stream()
                    .map(BookSummary::from)
                    .toList();

            return new BookRecommendationResponse(bestSellerDtos, topRatedDtos);
        }

        public List<BookSummary> getBestSellers() {
            return bestSellers;
        }

        public List<BookSummary> getTopRated() {
            return topRated;
        }
    }

    // ====== DTO: 책 요약 ======
    @Schema(name = "BookSummary", description = "도서 요약 정보 DTO")
    public static class BookSummary {

        @Schema(description = "도서 ID", example = "1")
        private Long id;

        @Schema(description = "도서 제목", example = "자바의 정석")
        private String title;

        @Schema(description = "저자명", example = "남궁성")
        private String author;

        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "도서 가격(원)", example = "25000")
        private int price;

        @Schema(description = "리뷰 개수", example = "12")
        private long reviewCount;

        @Schema(description = "평균 평점", example = "4.5")
        private double averageRating;

        public BookSummary(Long id, String title, String author,
                           Long categoryId, int price,
                           long reviewCount, double averageRating) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.categoryId = categoryId;
            this.price = price;
            this.reviewCount = reviewCount;
            this.averageRating = averageRating;
        }

        public static BookSummary from(Book book) {
            return new BookSummary(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategoryId(),
                    book.getPrice(),
                    book.getReviewCount(),
                    book.getAverageRating()
            );
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public int getPrice() {
            return price;
        }

        public long getReviewCount() {
            return reviewCount;
        }

        public double getAverageRating() {
            return averageRating;
        }
    }

    // ====== DTO: 공통 페이지 응답 ======
    @Schema(name = "PageResponse", description = "페이지네이션 응답 공통 DTO")
    public static class PageResponse<T> {

        @Schema(description = "현재 페이지 데이터 목록")
        private List<T> content;

        @Schema(description = "현재 페이지 번호(0부터 시작)", example = "0")
        private int page;

        @Schema(description = "페이지 크기", example = "10")
        private int size;

        @Schema(description = "전체 데이터 개수", example = "203")
        private long totalElements;

        @Schema(description = "전체 페이지 수", example = "21")
        private int totalPages;

        @Schema(description = "정렬 조건 (필드,방향)", example = "createdAt,DESC")
        private String sort;

        public PageResponse(List<T> content,
                            int page,
                            int size,
                            long totalElements,
                            int totalPages,
                            String sort) {
            this.content = content;
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.sort = sort;
        }

        public static <T> PageResponse<T> from(Page<T> page, String sort) {
            return new PageResponse<>(
                    page.getContent(),
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages(),
                    sort
            );
        }

        public List<T> getContent() {
            return content;
        }

        public int getPage() {
            return page;
        }

        public int getSize() {
            return size;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public String getSort() {
            return sort;
        }
    }
}
