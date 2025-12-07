package com.example.bookstore.auth.jwt;

import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /**
     * 현재 로그인한 사용자의 userId 를 반환.
     * - 인증 정보가 없거나
     * - anonymousUser 이거나
     * - userId를 숫자로 파싱할 수 없으면
     *   -> UNAUTHORIZED BusinessException 발생
     */
    public static Long getCurrentUserIdOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // JwtAuthenticationFilter 에서 userId 를 문자열로 넣어줬으므로
        String userIdStr = authentication.getName(); // 또는 principal.toString()

        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * 필요하면 현재 사용자의 role 을 문자열로 꺼내는 메서드도 만들 수 있음.
     * (지금은 안 써도 됨)
     */
    public static String getCurrentUserRoleOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        if (authentication.getAuthorities() == null || authentication.getAuthorities().isEmpty()) {
            return null;
        }
        return authentication.getAuthorities().iterator().next().getAuthority(); // "ROLE_USER" 등
    }
}
