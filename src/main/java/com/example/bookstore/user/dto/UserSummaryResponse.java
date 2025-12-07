package com.example.bookstore.user.dto;

import com.example.bookstore.user.entity.User;

import java.time.LocalDateTime;

public class UserSummaryResponse {

    private Long userId;
    private String email;
    private String name;
    private String role;
    private String userStatus;
    private LocalDateTime createdAt;

    public static UserSummaryResponse from(User user) {
        UserSummaryResponse dto = new UserSummaryResponse();
        dto.userId = user.getId();
        dto.email = user.getEmail();
        dto.name = user.getName();
        dto.role = user.getRole().name();
        dto.userStatus = user.getUserStatus().name();
        dto.createdAt = user.getCreatedAt();
        return dto;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
