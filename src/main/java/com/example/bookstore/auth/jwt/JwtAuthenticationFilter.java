package com.example.bookstore.auth.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 필요 없는 엔드포인트만 화이트리스트로 두자
    private static final String[] WHITE_LIST = {
            "/api/auth/signup",
            "/api/auth/login",
            "/api/auth/refresh",
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-ui/",
            "/swagger-ui.html",
            "/test-success",
            "/test-error",
            "/health"
    };

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        for (String pattern : WHITE_LIST) {
            if (path.startsWith(pattern)) {
                return true; // 이 경우에는 JWT 검사 안 함
            }
        }
        return false; // 나머지 모든 요청은 JWT 검사
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.parseClaims(token);

                String userId = claims.getSubject();        // sub
                String role = claims.get("role", String.class); // "ROLE_USER" / "ROLE_ADMIN"

                // SecurityContext에 Authentication 세팅
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userId,          // principal (우리는 userId 문자열)
                        null,            // credentials (비밀번호는 필요 없음)
                        List.of(new SimpleGrantedAuthority(role))
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
