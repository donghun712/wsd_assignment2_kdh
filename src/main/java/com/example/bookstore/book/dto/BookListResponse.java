package com.example.bookstore.book.dto;

import com.example.bookstore.book.entity.Book;

import java.time.LocalDateTime;

public class BookListResponse {

    private Long id;
    private String title;
    private int price;
    private int stock;
    private double averageRating;
    private long reviewCount;
    private Long categoryId;
    private LocalDateTime createdAt;

    public BookListResponse(Long id,
                            String title,
                            int price,
                            int stock,
                            double averageRating,
                            long reviewCount,
                            Long categoryId,
                            LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
    }

    public static BookListResponse from(Book book) {
        return new BookListResponse(
                book.getId(),
                book.getTitle(),
                book.getPrice(),         // int
                book.getStock(),         // int
                book.getAverageRating(), // double
                book.getReviewCount(),   // long
                book.getCategoryId(),    // Long
                book.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }
    public double getAverageRating() {
        return averageRating;
    }
    public long getReviewCount() {
        return reviewCount;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
