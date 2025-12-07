package com.example.bookstore.order.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")  // DB order_items 테이블과 매핑
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 주문에 속하는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 어떤 책인지 (books.id)
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private int quantity;

    // DB 컬럼: unit_price  ← 이름 맞춰주기!
    @Column(name = "unit_price", nullable = false)
    private int price;

    public OrderItem() {}

    public OrderItem(Long bookId, int quantity, int price) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
    }

    // ===== getter / setter =====

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
