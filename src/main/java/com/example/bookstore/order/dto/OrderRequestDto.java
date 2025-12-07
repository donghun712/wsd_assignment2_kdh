package com.example.bookstore.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderRequestDto {

    // 주문 생성 요청 DTO
    public static class CreateOrderRequest {

        @NotNull
        private List<OrderItemRequest> items;

        public CreateOrderRequest() {
        }

        public CreateOrderRequest(List<OrderItemRequest> items) {
            this.items = items;
        }

        public List<OrderItemRequest> getItems() {
            return items;
        }
    }

    // 요청에서 각 아이템 요소
    public static class OrderItemRequest {

        @NotNull
        private Long bookId;

        @Min(1)
        private int quantity;

        public OrderItemRequest() {
        }

        public OrderItemRequest(Long bookId, int quantity) {
            this.bookId = bookId;
            this.quantity = quantity;
        }

        public Long getBookId() {
            return bookId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
