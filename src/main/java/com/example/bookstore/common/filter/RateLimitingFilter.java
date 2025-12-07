package com.example.bookstore.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 간단한 IP 기반 레이트 리밋 필터
 * - 같은 IP 에서 1분 동안 100개 초과 요청이 들어오면 429 반환
 * - 과제용 "보안/성능" 요구사항 충족용 (실서비스용 아님)
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static class Window {
        long windowStartMillis;
        int count;

        Window(long windowStartMillis, int count) {
            this.windowStartMillis = windowStartMillis;
            this.count = count;
        }
    }

    // IP별 요청 카운터
    private final Map<String, Window> buckets = new ConcurrentHashMap<>();

    // 설정값 (원하면 나중에 @Value로 뺄 수 있음)
    private static final long WINDOW_MILLIS = 60_000L; // 1분
    private static final int LIMIT_PER_WINDOW = 100;   // 1분당 최대 100요청

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Preflight(OPTIONS)는 레이트 리밋 대상에서 제외
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = getClientIp(request);
        long now = System.currentTimeMillis();

        Window window = buckets.computeIfAbsent(clientIp, ip -> new Window(now, 0));

        synchronized (window) {
            // 새 윈도우로 리셋 (1분 지났으면 초기화)
            if (now - window.windowStartMillis >= WINDOW_MILLIS) {
                window.windowStartMillis = now;
                window.count = 0;
            }

            window.count++;

            if (window.count > LIMIT_PER_WINDOW) {
                // 429 Too Many Requests 응답
                writeTooManyRequests(response, request.getRequestURI());
                return;
            }
        }

        // 제한에 걸리지 않으면 정상 진행
        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        // 프록시 헤더가 있으면 우선 사용 (있다면)
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private void writeTooManyRequests(HttpServletResponse response, String path) throws IOException {
        response.setStatus(429);
        response.setContentType("application/json;charset=UTF-8");

        String body = """
                {
                  "timestamp": "%s",
                  "path": "%s",
                  "status": 429,
                  "code": "TOO_MANY_REQUESTS",
                  "message": "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."
                }
                """.formatted(Instant.now().toString(), path);

        response.getWriter().write(body);
    }
}
