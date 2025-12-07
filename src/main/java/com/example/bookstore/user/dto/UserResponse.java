package com.example.bookstore.user.dto;

import com.example.bookstore.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserResponse {

    private Long userId;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate;
    private String gender;
    private String region;
    private String role;
    private String userStatus;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        UserResponse dto = new UserResponse();
        dto.userId = user.getId();
        dto.email = user.getEmail();
        dto.name = user.getName();
        dto.phoneNumber = user.getPhoneNumber();
        dto.address = user.getAddress();
        dto.birthDate = user.getBirthDate();
        dto.gender = user.getGender() != null ? user.getGender().name().toLowerCase() : null;
        dto.region = user.getRegion();
        dto.role = user.getRole() != null ? user.getRole().name() : null;
        dto.userStatus = user.getUserStatus() != null ? user.getUserStatus().name() : null;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getRegion() {
        return region;
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
