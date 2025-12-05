package com.example.bookstore.auth.dto;

public class LoginResponse {

    private Long userId;
    private String accessToken;
    private String refreshToken;

    public LoginResponse(Long userId, String accessToken, String refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return userId;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
}
