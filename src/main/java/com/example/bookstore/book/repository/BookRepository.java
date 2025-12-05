package com.example.bookstore.book.repository;

import com.example.bookstore.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
