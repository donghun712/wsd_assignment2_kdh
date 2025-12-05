package com.example.bookstore.book.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "average_rating", nullable = false)
    private double averageRating;

    @Column(name = "review_count", nullable = false)
    private long reviewCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Book() {}

    // ===== Getter =====

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
    public Long getCategoryId() { return categoryId; }
    public double getAverageRating() { return averageRating; }
    public long getReviewCount() { return reviewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ===== Setter (리뷰 갱신에 필요) =====

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // ===== JPA Event =====

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (Double.isNaN(this.averageRating)) this.averageRating = 0.0;
        if (this.reviewCount < 0) this.reviewCount = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
