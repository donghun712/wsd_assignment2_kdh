package com.example.bookstore.user.dto;

import java.time.LocalDateTime;

public class UpdateMyInfoResponse {

    private Long userId;
    private LocalDateTime updatedAt;

    public UpdateMyInfoResponse(Long userId, LocalDateTime updatedAt) {
        this.userId = userId;
        this.updatedAt = updatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
