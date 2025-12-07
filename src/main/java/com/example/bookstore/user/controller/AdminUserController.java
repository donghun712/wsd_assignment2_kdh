package com.example.bookstore.user.controller;

import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.common.response.ApiResponse;
import com.example.bookstore.user.dto.UserResponse;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import com.example.bookstore.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "Admin User API", description = "관리자용 유저 관리 API")
public class AdminUserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public AdminUserController(UserRepository userRepository,
                               UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * 관리자용 전체 유저 목록 조회 (페이지네이션)
     */
    @GetMapping
    @Operation(
            summary = "전체 유저 조회(관리자)",
            description = "관리자 권한으로 모든 사용자 목록을 페이지네이션 형태로 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "유저 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "관리자 권한이 아닌 경우 접근 불가"
            )
    })
    public ApiResponse<Page<UserResponse>> getUsers(
            @Parameter(hidden = true)
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserResponse> dtoPage = users.map(UserResponse::from);
        return ApiResponse.success(dtoPage);
    }

    /**
     * 관리자용 단일 유저 상세 조회
     */
    @GetMapping("/{userId}")
    @Operation(
            summary = "단일 유저 상세 조회(관리자)",
            description = "관리자가 특정 사용자의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "유저 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없음"
            )
    })
    public ApiResponse<UserResponse> getUser(
            @Parameter(description = "조회할 유저 ID", example = "1")
            @PathVariable Long userId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return ApiResponse.success(UserResponse.from(user));
    }

    /**
     * 관리자용 사용자 비활성화
     * 예) PATCH /api/admin/users/5/deactivate
     */
    @PatchMapping("/{userId}/deactivate")
    @Operation(
            summary = "유저 비활성화(관리자)",
            description = "관리자가 특정 사용자의 상태를 비활성(INACTIVE)으로 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "유저 비활성화 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "유저를 찾을 수 없음"
            )
    })
    public ApiResponse<Void> deactivateUser(
            @Parameter(description = "비활성화할 유저 ID", example = "5")
            @PathVariable Long userId
    ) {
        userService.deactivateUser(userId);
        return ApiResponse.success(null);
    }
}
