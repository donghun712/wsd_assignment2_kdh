package com.example.bookstore.user.service;

import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.user.dto.ChangePasswordRequest;
import com.example.bookstore.user.dto.ChangePasswordResponse;
import com.example.bookstore.user.dto.UpdateMyInfoRequest;
import com.example.bookstore.user.dto.UpdateMyInfoResponse;
import com.example.bookstore.user.dto.UserMeResponse;
import com.example.bookstore.user.dto.UserSummaryResponse;
import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.entity.UserStatus;
import com.example.bookstore.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * SecurityContext 에서 현재 로그인한 사용자 ID 가져오기
     */
    private Long getCurrentUserIdOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Long) {
            return (Long) principal;
        }
        if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                // fall-through 아래에서 예외 처리
            }
        }

        throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증 정보에서 사용자 ID를 찾을 수 없습니다.");
    }

    /**
     * 현재 로그인한 사용자의 정보 조회
     */
    @Transactional(readOnly = true)
    public UserMeResponse getMyInfo() {
        Long userId = getCurrentUserIdOrThrow();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserMeResponse.from(user);
    }

    /**
     * 현재 로그인한 사용자의 프로필 수정
     */
    public UpdateMyInfoResponse updateMyInfo(UpdateMyInfoRequest request) {
        Long userId = getCurrentUserIdOrThrow();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Gender gender = request.getGender() != null ? Gender.from(request.getGender()) : null;

        user.updateProfile(
                request.getName(),
                request.getPhoneNumber(),
                request.getAddress(),
                request.getRegion(),
                gender
        );

        return new UpdateMyInfoResponse(user.getId(), user.getUpdatedAt());
    }

    /**
     * 비밀번호 변경
     */
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        Long userId = getCurrentUserIdOrThrow();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        return new ChangePasswordResponse(user.getId(), user.getUpdatedAt());
    }

    /**
     * 관리자용 사용자 목록 조회
     * - status 파라미터가 있으면 메모리에서 필터링
     */
    @Transactional(readOnly = true)
    public Page<UserSummaryResponse> getUsers(Pageable pageable, UserStatus status) {
        Page<User> page = userRepository.findAll(pageable);

        if (status == null) {
            return page.map(UserSummaryResponse::from);
        }

        List<UserSummaryResponse> filtered = page.getContent().stream()
                .filter(user -> user.getUserStatus() == status)
                .map(UserSummaryResponse::from)
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    /**
     * 관리자용 사용자 비활성화
     */
    public void deactivateUser(Long targetUserId) {
        User user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (user.getUserStatus() == UserStatus.INACTIVE) {
            throw new BusinessException(ErrorCode.STATE_CONFLICT, "이미 비활성화된 사용자입니다.");
        }

        user.deactivate();
    }
}
