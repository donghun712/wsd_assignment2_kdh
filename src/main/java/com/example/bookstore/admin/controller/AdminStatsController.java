package com.example.bookstore.admin.controller;

import com.example.bookstore.admin.dto.AdminStatsSummaryResponse;
import com.example.bookstore.admin.dto.AdminUserStatsResponse;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.response.ApiResponse;
import com.example.bookstore.order.repository.OrderRepository;
import com.example.bookstore.review.repository.ReviewRepository;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.entity.UserStatus;
import com.example.bookstore.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
@Tag(name = "Admin Stats API", description = "관리자용 요약/사용자 통계 API")
public class AdminStatsController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public AdminStatsController(UserRepository userRepository,
                                BookRepository bookRepository,
                                OrderRepository orderRepository,
                                ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * 전체 요약 통계
     * GET /api/admin/stats/summary
     */
    @GetMapping("/summary")
    @Operation(
            summary = "전체 요약 통계 조회",
            description = "유저 수, 도서 수, 주문 수, 리뷰 수, 활성/비활성 유저 수를 한 번에 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "요약 통계 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminStatsSummaryResponse.class)
                    )
            )
    })
    public ApiResponse<AdminStatsSummaryResponse> getSummary() {

        long userCount = userRepository.count();
        long bookCount = bookRepository.count();
        long orderCount = orderRepository.count();
        long reviewCount = reviewRepository.count();

        List<User> users = userRepository.findAll();
        long activeUserCount = users.stream()
                .filter(u -> u.getUserStatus() == UserStatus.ACTIVE)
                .count();
        long inactiveUserCount = users.stream()
                .filter(u -> u.getUserStatus() == UserStatus.INACTIVE)
                .count();

        AdminStatsSummaryResponse response = new AdminStatsSummaryResponse(
                userCount,
                bookCount,
                orderCount,
                reviewCount,
                activeUserCount,
                inactiveUserCount
        );

        return ApiResponse.success(response);
    }

    /**
     * 사용자 상태 통계만 별도로 조회
     * GET /api/admin/stats/users
     */
    @GetMapping("/users")
    @Operation(
            summary = "사용자 상태 통계 조회",
            description = "활성/비활성 사용자 수만 별도로 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "사용자 상태 통계 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminUserStatsResponse.class)
                    )
            )
    })
    public ApiResponse<AdminUserStatsResponse> getUserStats() {

        List<User> users = userRepository.findAll();
        long activeUserCount = users.stream()
                .filter(u -> u.getUserStatus() == UserStatus.ACTIVE)
                .count();
        long inactiveUserCount = users.stream()
                .filter(u -> u.getUserStatus() == UserStatus.INACTIVE)
                .count();

        AdminUserStatsResponse response =
                new AdminUserStatsResponse(activeUserCount, inactiveUserCount);

        return ApiResponse.success(response);
    }
}
