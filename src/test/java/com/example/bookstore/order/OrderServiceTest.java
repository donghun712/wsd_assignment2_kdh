package com.example.bookstore.order;

import com.example.bookstore.order.dto.OrderRequestDto;
import com.example.bookstore.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    @DisplayName("주문 생성 실패 - 요청이 null")
    void createOrder_fail_nullRequest() {
        assertThrows(RuntimeException.class, () ->
                orderService.createOrder(1L, null)
        );
    }

    @Test
    @DisplayName("주문 생성 실패 - 아이템 목록이 비어 있음")
    void createOrder_fail_emptyItems() {
        OrderRequestDto.CreateOrderRequest req =
                new OrderRequestDto.CreateOrderRequest(Collections.emptyList());

        assertThrows(RuntimeException.class, () ->
                orderService.createOrder(1L, req)
        );
    }

    @Test
    @DisplayName("주문 생성 실패 - 수량이 0 이하")
    void createOrder_fail_quantityNonPositive() {
        OrderRequestDto.OrderItemRequest item =
                new OrderRequestDto.OrderItemRequest(1L, 0); // quantity = 0

        OrderRequestDto.CreateOrderRequest req =
                new OrderRequestDto.CreateOrderRequest(List.of(item));

        assertThrows(RuntimeException.class, () ->
                orderService.createOrder(1L, req)
        );
    }
}
