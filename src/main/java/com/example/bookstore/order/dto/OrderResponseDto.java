package com.example.bookstore.order.dto;

import com.example.bookstore.order.OrderStatus;
import com.example.bookstore.order.entity.Order;
import com.example.bookstore.order.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    // 주문 상세 / 생성 결과에 사용할 DTO
    public static class OrderDetailResponse {
        private Long id;
        private int totalPrice;
        private OrderStatus status;
        private LocalDateTime createdAt;
        private List<OrderItemResponse> items;

        public OrderDetailResponse(Long id, int totalPrice, OrderStatus status,
                                   LocalDateTime createdAt, List<OrderItemResponse> items) {
            this.id = id;
            this.totalPrice = totalPrice;
            this.status = status;
            this.createdAt = createdAt;
            this.items = items;
        }

        public static OrderDetailResponse from(Order order) {
            List<OrderItemResponse> itemDtos = order.getItems().stream()
                    .map(OrderItemResponse::from)
                    .toList();

            return new OrderDetailResponse(
                    order.getId(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    order.getCreatedAt(),
                    itemDtos
            );
        }

        public Long getId() { return id; }
        public int getTotalPrice() { return totalPrice; }
        public OrderStatus getStatus() { return status; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public List<OrderItemResponse> getItems() { return items; }
    }

    // 주문 목록에서 사용할 요약 DTO
    public static class OrderSummaryResponse {
        private Long id;
        private int totalPrice;
        private OrderStatus status;
        private LocalDateTime createdAt;

        public OrderSummaryResponse(Long id, int totalPrice,
                                    OrderStatus status, LocalDateTime createdAt) {
            this.id = id;
            this.totalPrice = totalPrice;
            this.status = status;
            this.createdAt = createdAt;
        }

        public static OrderSummaryResponse from(Order order) {
            return new OrderSummaryResponse(
                    order.getId(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    order.getCreatedAt()
            );
        }

        public Long getId() { return id; }
        public int getTotalPrice() { return totalPrice; }
        public OrderStatus getStatus() { return status; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }

    // 각 주문 아이템 DTO
    public static class OrderItemResponse {
        private Long id;
        private Long bookId;
        private int quantity;
        private int price;

        public OrderItemResponse(Long id, Long bookId, int quantity, int price) {
            this.id = id;
            this.bookId = bookId;
            this.quantity = quantity;
            this.price = price;
        }

        public static OrderItemResponse from(OrderItem item) {
            return new OrderItemResponse(
                    item.getId(),
                    item.getBookId(),
                    item.getQuantity(),
                    item.getPrice()
            );
        }

        public Long getId() { return id; }
        public Long getBookId() { return bookId; }
        public int getQuantity() { return quantity; }
        public int getPrice() { return price; }
    }
}
