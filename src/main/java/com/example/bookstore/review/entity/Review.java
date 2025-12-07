package com.example.bookstore.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 어떤 유저가 작성했는지 (NOT NULL)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 🔹 어떤 책에 대한 리뷰인지
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    // 🔹 평점 (1~5 등)
    @Column(nullable = false)
    private int rating;

    // 🔹 리뷰 내용
    @Column(nullable = false, length = 1000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // ✅ 리뷰 수정에 필요: rating/content 변경 메서드 추가

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
