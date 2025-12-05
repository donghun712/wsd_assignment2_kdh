package com.example.bookstore.order;

public enum OrderStatus {
    PENDING,    // 결제 전 / 주문 생성 직후
    PAID,       // 결제 완료
    SHIPPED,    // 배송 중
    COMPLETED,  // 주문 완료
    CANCELLED   // 주문 취소
}
