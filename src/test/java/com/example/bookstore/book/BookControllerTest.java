package com.example.bookstore.book;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("전체 도서 목록 조회 성공")
    void getAllBooks_success() throws Exception {
        mockMvc.perform(
                        get("/api/books")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("도서 상세 조회 성공 - 존재하는 ID")
    void getBookDetail_success() throws Exception {
        // 시드 데이터 기준으로 1번 도서는 존재한다고 가정
        mockMvc.perform(
                        get("/api/books/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.id").value(1));
    }

    @Test
    @DisplayName("도서 검색 성공 - 키워드 없이 기본 조회")
    void searchBooks_success() throws Exception {
        mockMvc.perform(
                        get("/api/books/search")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
