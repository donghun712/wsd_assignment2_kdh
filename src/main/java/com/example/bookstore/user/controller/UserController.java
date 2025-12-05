package com.example.bookstore.user.controller;

import com.example.bookstore.common.response.ApiResponse;
import com.example.bookstore.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ApiResponse<UserMeResponse> me() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        UserMeResponse dto = new UserMeResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );

        return ApiResponse.success(dto);
    }

    public static class UserMeResponse {
        private Long id;
        private String email;
        private String name;
        private String role;

        public UserMeResponse(Long id, String email, String name, String role) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.role = role;
        }

        public Long getId() { return id; }
        public String getEmail() { return email; }
        public String getName() { return name; }
        public String getRole() { return role; }
    }
}
