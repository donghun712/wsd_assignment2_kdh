package com.example.bookstore.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ReviewUpdateRequest {

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private String content;

    public ReviewUpdateRequest() {
    }

    public ReviewUpdateRequest(int rating, String content) {
        this.rating = rating;
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
