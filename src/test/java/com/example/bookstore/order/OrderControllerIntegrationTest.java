package com.example.bookstore.order;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerIntegrationTest extends IntegrationTestSupport {

    @Test
    @DisplayName("주문 생성 실패 - 토큰 없음")
    void createOrder_fail_unauthenticated() throws Exception {
        String body = """
                {
                  "items": [
                    { "bookId": 1, "quantity": 1 }
                  ]
                }
                """;

        mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("내 주문 목록 조회 실패 - 토큰 없음")
    void getMyOrders_fail_unauthenticated() throws Exception {
        mockMvc.perform(
                        get("/api/orders/my")
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("내 주문 목록 조회 성공 - 로그인 상태")
    void getMyOrders_success_authenticated() throws Exception {
        mockMvc.perform(
                        get("/api/orders/my")
                                .header("Authorization", bearer(userAccessToken))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
