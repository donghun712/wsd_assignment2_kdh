package com.example.bookstore.order.controller;

import com.example.bookstore.common.response.ApiResponse;
import com.example.bookstore.order.OrderStatus;
import com.example.bookstore.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@Tag(name = "Admin Order API", description = "관리자용 주문 관리 API")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 관리자 – 전체 주문 목록 조회 (페이지네이션)
     * GET /api/admin/orders
     */
    @GetMapping
    @Operation(
            summary = "전체 주문 목록 조회(관리자)",
            description = "관리자가 모든 주문 내역을 페이지네이션 형태로 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "주문 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "관리자 권한이 없는 경우 접근 불가"
            )
    })
    public ApiResponse<Page<OrderService.OrderResponse>> getAllOrders(
            @Parameter(hidden = true) Pageable pageable
    ) {
        Page<OrderService.OrderResponse> page = orderService.getAllOrdersForAdmin(pageable);
        return ApiResponse.success(page);
    }

    /**
     * 관리자 – 주문 상세 조회
     * GET /api/admin/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    @Operation(
            summary = "주문 상세 조회(관리자)",
            description = "관리자가 특정 주문의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "주문 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderService.OrderResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 주문을 찾을 수 없음"
            )
    })
    public ApiResponse<OrderService.OrderResponse> getOrder(
            @Parameter(description = "조회할 주문 ID", example = "1")
            @PathVariable Long orderId
    ) {
        return ApiResponse.success(orderService.getOrderForAdmin(orderId));
    }

    /**
     * 관리자 – 주문 상태 변경
     * PATCH /api/admin/orders/{orderId}/status
     */
    @PatchMapping("/{orderId}/status")
    @Operation(
            summary = "주문 상태 변경(관리자)",
            description = "관리자가 특정 주문의 상태(예: 준비중, 배송중, 완료 등)를 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "주문 상태 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderService.OrderResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 상태 값 또는 요청 형식"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 주문을 찾을 수 없음"
            )
    })
    public ApiResponse<OrderService.OrderResponse> updateOrderStatus(
            @Parameter(description = "상태를 변경할 주문 ID", example = "1")
            @PathVariable Long orderId,
            @RequestBody UpdateStatusRequest request
    ) {
        OrderService.OrderResponse response =
                orderService.updateOrderStatus(orderId, request.getStatus());
        return ApiResponse.success(response);
    }

    @Schema(name = "UpdateStatusRequest", description = "주문 상태 변경 요청 DTO")
    public static class UpdateStatusRequest {

        @Schema(description = "변경할 주문 상태", example = "SHIPPED")
        private OrderStatus status;

        public OrderStatus getStatus() {
            return status;
        }
        public void setStatus(OrderStatus status) {
            this.status = status;
        }
    }
}
