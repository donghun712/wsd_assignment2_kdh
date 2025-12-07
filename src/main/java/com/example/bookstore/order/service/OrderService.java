package com.example.bookstore.order.service;

import com.example.bookstore.book.entity.Book;
import com.example.bookstore.book.repository.BookRepository;
import com.example.bookstore.common.exception.BusinessException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.order.OrderStatus;
import com.example.bookstore.order.dto.OrderRequestDto;
import com.example.bookstore.order.entity.Order;
import com.example.bookstore.order.entity.OrderItem;
import com.example.bookstore.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    public OrderService(OrderRepository orderRepository,
                        BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * 1) 주문 생성 (일반 사용자)
     */
    public OrderResponse createOrder(Long userId, OrderRequestDto.CreateOrderRequest request) {

        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, Map.of(
                    "items", "주문 항목이 비어 있습니다."
            ));
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);

        int totalPrice = 0;

        for (OrderRequestDto.OrderItemRequest itemReq : request.getItems()) {
            Long bookId = itemReq.getBookId();
            int quantity = itemReq.getQuantity();

            if (quantity <= 0) {
                throw new BusinessException(ErrorCode.BAD_REQUEST, Map.of(
                        "quantity", "수량은 1 이상이어야 합니다."
                ));
            }

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

            int unitPrice = book.getPrice();
            int linePrice = unitPrice * quantity;
            totalPrice += linePrice;

            OrderItem orderItem = new OrderItem(book.getId(), quantity, unitPrice);
            order.addItem(orderItem);
        }

        order.setTotalPrice(totalPrice);

        Order saved = orderRepository.save(order);

        return OrderResponse.from(saved);
    }

    /**
     * 2) 내 주문 목록 조회
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(OrderResponse::from)
                .toList();
    }

    /**
     * 3) 내 주문 단건 조회 (내 소유 여부 검사)
     */
    @Transactional(readOnly = true)
    public OrderResponse getMyOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return OrderResponse.from(order);
    }

    // ================== 관리자용 ==================

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrdersForAdmin(Pageable pageable) {
        Page<Order> page = orderRepository.findAll(pageable);
        return page.map(OrderResponse::from);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderForAdmin(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        return OrderResponse.from(order);
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        order.setStatus(newStatus);
        return OrderResponse.from(order);
    }

    // ================== DTO ==================

    public static class OrderResponse {
        private Long id;
        private Long userId;
        private int totalPrice;
        private String status;
        private LocalDateTime createdAt;
        private List<OrderItemResponse> items;

        public OrderResponse(Long id,
                             Long userId,
                             int totalPrice,
                             String status,
                             LocalDateTime createdAt,
                             List<OrderItemResponse> items) {
            this.id = id;
            this.userId = userId;
            this.totalPrice = totalPrice;
            this.status = status;
            this.createdAt = createdAt;
            this.items = items;
        }

        public static OrderResponse from(Order order) {
            List<OrderItemResponse> itemDtos = order.getItems().stream()
                    .map(OrderItemResponse::from)
                    .toList();

            return new OrderResponse(
                    order.getId(),
                    order.getUserId(),
                    order.getTotalPrice(),
                    order.getStatus().name(),
                    order.getCreatedAt(),
                    itemDtos
            );
        }

        public Long getId() { return id; }
        public Long getUserId() { return userId; }
        public int getTotalPrice() { return totalPrice; }
        public String getStatus() { return status; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public List<OrderItemResponse> getItems() { return items; }
    }

    public static class OrderItemResponse {
        private Long bookId;
        private int quantity;
        private int price;

        public OrderItemResponse(Long bookId, int quantity, int price) {
            this.bookId = bookId;
            this.quantity = quantity;
            this.price = price;
        }

        public static OrderItemResponse from(OrderItem item) {
            return new OrderItemResponse(
                    item.getBookId(),
                    item.getQuantity(),
                    item.getPrice()
            );
        }

        public Long getBookId() { return bookId; }
        public int getQuantity() { return quantity; }
        public int getPrice() { return price; }
    }
}
