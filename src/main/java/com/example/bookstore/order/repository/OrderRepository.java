package com.example.bookstore.order.repository;

import com.example.bookstore.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 내 주문 목록 (최신 순)
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

     // 특정 유저의 특정 주문 1건
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
