package com.example.bookstore.admin.dto;

public class AdminStatsSummaryResponse {

    private long userCount;
    private long bookCount;
    private long orderCount;
    private long reviewCount;
    private long activeUserCount;
    private long inactiveUserCount;

    public AdminStatsSummaryResponse(long userCount,
                                     long bookCount,
                                     long orderCount,
                                     long reviewCount,
                                     long activeUserCount,
                                     long inactiveUserCount) {
        this.userCount = userCount;
        this.bookCount = bookCount;
        this.orderCount = orderCount;
        this.reviewCount = reviewCount;
        this.activeUserCount = activeUserCount;
        this.inactiveUserCount = inactiveUserCount;
    }

    public long getUserCount() {
        return userCount;
    }

    public long getBookCount() {
        return bookCount;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public long getActiveUserCount() {
        return activeUserCount;
    }

    public long getInactiveUserCount() {
        return inactiveUserCount;
    }
}
