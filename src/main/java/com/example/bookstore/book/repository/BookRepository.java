package com.example.bookstore.book.repository;

import com.example.bookstore.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // 1) 카테고리별 조회
    List<Book> findByCategoryId(Long categoryId);

    // 2) TOP 도서 (평점, 리뷰수 기준) – 10개
    List<Book> findTop10ByOrderByAverageRatingDescReviewCountDesc();

    // 3) 제목 또는 저자에 keyword가 포함된 도서 검색 (페이지네이션)
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String titleKeyword,
            String authorKeyword,
            Pageable pageable
    );

    // 4) 주문 데이터 기반 베스트셀러 조회 (상위 N권)
    @Query(
            value = """
                    SELECT b.* 
                    FROM books b
                    JOIN order_items oi ON oi.book_id = b.id
                    GROUP BY b.id
                    ORDER BY SUM(oi.quantity) DESC
                    LIMIT :limit
                    """,
            nativeQuery = true
    )
    List<Book> findBestSellerBooks(@Param("limit") int limit);

    // 5) 검색 + 카테고리 필터 + 페이지네이션/정렬
    @Query("""
            SELECT b
            FROM Book b
            WHERE
              (:keyword IS NULL
               OR LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:categoryId IS NULL OR b.categoryId = :categoryId)
            """)
    Page<Book> searchBooks(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}
