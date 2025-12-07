package com.example.bookstore.auth.service;

import com.example.bookstore.auth.dto.LoginRequest;
import com.example.bookstore.auth.dto.LoginResponse;
import com.example.bookstore.auth.dto.RefreshTokenRequest;
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

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 회원가입
     */
    public SignupResponse signup(SignupRequest request) {

        // 이미 가입된 이메일인지 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException(
                    ErrorCode.DUPLICATE_RESOURCE,
                    Map.of("email", "이미 가입된 이메일입니다.")
            );
        }

        // User 엔티티 생성 + 비밀번호 암호화
        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                Role.ROLE_USER   // 기본 USER 롤
        );

        User saved = userRepository.save(user);

        return new SignupResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getName(),
                saved.getRole().name()
        );
    }

    /**
     * 로그인: 이메일/비밀번호 검증 후 액세스/리프레시 토큰 발급
     */
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

        // 3) JWT 발급
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole().name());

        return new LoginResponse(
                user.getId(),
                accessToken,
                refreshToken
        );
    }

    /**
     * 리프레시 토큰으로 액세스 토큰 재발급
     */
    public LoginResponse refresh(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        // 1) 토큰 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(
                    ErrorCode.UNAUTHORIZED,
                    Map.of("token", "유효하지 않은 리프레시 토큰입니다.")
            );
        }

        // 2) 토큰에서 유저 정보 추출
        Long userId = jwtUtil.getUserId(refreshToken);
        String roleFromToken = jwtUtil.getRole(refreshToken); // "ROLE_USER" 등

        // 3) 실제 유저 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", "존재하지 않는 사용자입니다.")
                ));

        // 4) 새 액세스 토큰 발급
        //    (role은 토큰에서 꺼낸 값이나 user.getRole().name() 둘 다 가능)
        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), roleFromToken);

        // 5) 리프레시 토큰은 그대로 반환 (원하면 여기서 새로 발급해도 됨)
        String newRefreshToken = refreshToken;

        return new LoginResponse(
                user.getId(),
                newAccessToken,
                newRefreshToken
        );
    }
}
