package com.example.bookstore.review;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("리뷰 목록 조회 성공 - 비로그인도 가능")
    void getReviews_success() throws Exception {
        mockMvc.perform(
                        get("/api/books/1/reviews")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 토큰 없음")
    void createReview_fail_unauthenticated() throws Exception {
        String body = """
                {
                  "rating": 5,
                  "content": "테스트 리뷰입니다."
                }
                """;

        mockMvc.perform(
                        post("/api/books/1/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("리뷰 삭제 실패 - 토큰 없음")
    void deleteReview_fail_unauthenticated() throws Exception {
        mockMvc.perform(
                        delete("/api/books/1/reviews/1")
                )
                .andExpect(status().is4xxClientError());
    }
}
