package com.example.bookstore.auth.dto;

public class SignupResponse {

    private Long id;
    private String email;
    private String name;
    private String role;

    public SignupResponse(Long id, String email, String name, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
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
}
