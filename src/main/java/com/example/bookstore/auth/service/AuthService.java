package com.example.bookstore.auth.service;

import com.example.bookstore.auth.dto.LoginRequest;
import com.example.bookstore.auth.dto.LoginResponse;
import com.example.bookstore.auth.dto.SignupRequest;
import com.example.bookstore.auth.dto.SignupResponse;
import com.example.bookstore.auth.jwt.JwtUtil;
import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.user.entity.Role;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;   // ✅ JWT 유틸 주입

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {          // ✅ 생성자에서 같이 받기
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 회원가입
    public SignupResponse signup(SignupRequest request) {

        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new BusinessException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    Map.of("email", "이미 가입된 이메일입니다.")
            );
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(Role.ROLE_USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        return new SignupResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getName(),
                saved.getRole().name()
        );
    }

    // 로그인
    public LoginResponse login(LoginRequest request) {

        // 1) 이메일로 유저 찾기
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("email", "가입되지 않은 이메일입니다.")
                ));

        // 2) 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(
                    ErrorCode.UNAUTHORIZED,
                    Map.of("password", "비밀번호가 일치하지 않습니다.")
            );
        }

        // 3) JWT 발급 (여기서 jwtUtil 사용)
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole().name());

        return new LoginResponse(
                user.getId(),
                accessToken,
                refreshToken
        );
    }
}
