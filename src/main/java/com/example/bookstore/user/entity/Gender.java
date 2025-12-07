package com.example.bookstore.user.entity;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    public static Gender from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Gender.valueOf(value.toUpperCase());
    }
}
