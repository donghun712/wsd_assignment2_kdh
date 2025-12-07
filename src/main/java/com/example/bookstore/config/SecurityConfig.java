package com.example.bookstore.config;

import com.example.bookstore.auth.jwt.JwtAuthenticationFilter;
import com.example.bookstore.auth.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ✅ CORS: 위에서 정의한 CorsConfigurationSource 를 사용
                .cors(Customizer.withDefaults())

                // 🔐 CSRF 비활성화 (REST API + JWT 환경)
                .csrf(csrf -> csrf.disable())

                // 🔐 세션 사용 안 함 (STATELESS)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 🔐 URL별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // CORS preflight 용 OPTIONS 전부 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/test-success", "/test-error").permitAll()

                        // 인증 필요 없는 엔드포인트
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()

                        // Swagger / Health
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**"
                        ).permitAll()
                        .requestMatchers("/health").permitAll()

                        // 사용자 관련 (로그인 필요)
                        .requestMatchers("/api/user/**").authenticated()

                        // 주문 관련 (로그인 필요)
                        .requestMatchers("/api/orders/**").authenticated()

                        // 관리자 전용
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 그 외
                        .anyRequest().authenticated()
                )

                // 폼 로그인, HTTP Basic 비활성화 (우리는 JWT만 사용)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        // ✅ JWT 인증 필터 등록
        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
