package com.example.bookstore.admin.dto;

public class AdminUserStatsResponse {

    private long activeUserCount;
    private long inactiveUserCount;

    public AdminUserStatsResponse(long activeUserCount, long inactiveUserCount) {
        this.activeUserCount = activeUserCount;
        this.inactiveUserCount = inactiveUserCount;
    }

    public long getActiveUserCount() {
        return activeUserCount;
    }

    public long getInactiveUserCount() {
        return inactiveUserCount;
    }
}
