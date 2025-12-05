package com.example.bookstore.config;

import com.example.bookstore.auth.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 🔹 CSRF 비활성화 (REST + JWT 환경)
                .csrf(csrf -> csrf.disable())

                // 🔹 세션은 사용하지 않음 (STATELESS)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 🔹 URL 별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 가능한 것들
                        .requestMatchers("/api/auth/**").permitAll()        // 로그인/회원가입 등
                        .requestMatchers("/api/books/**").permitAll()       // 도서/리뷰 관련 전체 오픈
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**"
                        ).permitAll()                                      // Swagger

                        // 그 외 나머지는 인증 필요
                        .anyRequest().authenticated()
                )

                // 🔹 폼 로그인 / httpBasic 미사용
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        // 🔹 JWT 필터 등록 (UsernamePasswordAuthenticationFilter 전에)
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
