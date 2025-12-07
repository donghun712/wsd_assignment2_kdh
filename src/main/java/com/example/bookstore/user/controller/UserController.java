package com.example.bookstore.user.controller;

import com.example.bookstore.common.response.ApiResponse;
import com.example.bookstore.user.dto.ChangePasswordRequest;
import com.example.bookstore.user.dto.ChangePasswordResponse;
import com.example.bookstore.user.dto.UpdateMyInfoRequest;
import com.example.bookstore.user.dto.UpdateMyInfoResponse;
import com.example.bookstore.user.dto.UserMeResponse;
import com.example.bookstore.user.service.UserService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "User API", description = "로그인한 사용자의 내 정보/수정/비밀번호 변경 API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- 내 정보 조회 ---
    @GetMapping("/user/me")
    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 기본 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserMeResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    public ApiResponse<UserMeResponse> getMyInfo() {
        UserMeResponse response = userService.getMyInfo();
        return ApiResponse.success(response, "USER_200", "사용자 정보를 조회했습니다.");
    }

    // --- 내 정보 수정 ---
    @PutMapping("/user/me")
    @Operation(
            summary = "내 정보 수정",
            description = "현재 로그인한 사용자의 이름, 연락처 등의 정보를 수정합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateMyInfoResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 입력 값"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    public ApiResponse<UpdateMyInfoResponse> updateMyInfo(
            @Valid @RequestBody UpdateMyInfoRequest request
    ) {
        UpdateMyInfoResponse response = userService.updateMyInfo(request);
        return ApiResponse.success(response, "USER_200", "사용자 정보가 업데이트되었습니다.");
    }

    // --- 비밀번호 변경 ---
    @PostMapping("/user/change-password")
    @Operation(
            summary = "비밀번호 변경",
            description = "현재 비밀번호를 검증한 뒤, 새 비밀번호로 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChangePasswordResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 입력 값"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "현재 비밀번호 불일치 등으로 인한 변경 실패"
            )
    })
    public ApiResponse<ChangePasswordResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        ChangePasswordResponse response = userService.changePassword(request);
        return ApiResponse.success(response);
    }
}
