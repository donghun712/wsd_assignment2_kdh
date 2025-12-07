package com.example.bookstore.order.controller;

import com.example.bookstore.common.response.ApiResponse;
import com.example.bookstore.order.dto.OrderRequestDto;
import com.example.bookstore.order.service.OrderService;
import com.example.bookstore.order.service.OrderService.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "사용자의 주문 생성 및 조회 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 1) 주문 생성
     * POST /api/orders
     */
    @PostMapping
    @Operation(
            summary = "주문 생성",
            description = "현재 로그인한 사용자의 요청 정보를 바탕으로 주문을 생성합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "주문 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 값이 올바르지 않음(주문 항목 비어 있음 등)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    public ApiResponse<OrderResponse> createOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "주문 생성 요청 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = OrderRequestDto.CreateOrderRequest.class)
                    )
            )
            @Valid @RequestBody OrderRequestDto.CreateOrderRequest request,
            @Parameter(hidden = true) Authentication authentication
    ) {
        Long userId = Long.valueOf(authentication.getName());
        OrderResponse response = orderService.createOrder(userId, request);
        return ApiResponse.success(response);
    }

    /**
     * 2) 내 주문 목록 조회
     */
    @GetMapping("/my")
    @Operation(
            summary = "내 주문 목록 조회",
            description = "현재 로그인한 사용자의 모든 주문 내역을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 주문 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            )
    })
    public ApiResponse<List<OrderResponse>> getMyOrders(
            @Parameter(hidden = true) Authentication authentication
    ) {
        Long userId = Long.valueOf(authentication.getName());
        return ApiResponse.success(orderService.getMyOrders(userId));
    }

    /**
     * 3) 내 주문 단건(상세) 조회
     */
    @GetMapping("/my/{orderId}")
    @Operation(
            summary = "내 주문 상세 조회",
            description = "현재 로그인한 사용자가 자신의 특정 주문 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 주문 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "다른 사용자의 주문에 접근하는 경우"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 주문을 찾을 수 없음"
            )
    })
    public ApiResponse<OrderResponse> getMyOrder(
            @Parameter(description = "조회할 주문 ID", example = "1")
            @PathVariable Long orderId,
            @Parameter(hidden = true) Authentication authentication
    ) {
        Long userId = Long.valueOf(authentication.getName());
        OrderResponse response = orderService.getMyOrder(userId, orderId);
        return ApiResponse.success(response);
    }
}
