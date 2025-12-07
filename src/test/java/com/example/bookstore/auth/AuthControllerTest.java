package com.example.bookstore.auth;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// 실패 케이스에서는 body 구조까지 안 볼 거라 jsonPath는 성공 케이스에서만 써도 됨
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class AuthControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("로그인 성공 - 정상적인 이메일/비밀번호")
    void login_success() throws Exception {
        String body = """
                {
                  "email": "justuser@example.com",
                  "password": "new123456"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.accessToken").isString());
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_wrongPassword() throws Exception {
        String body = """
                {
                  "email": "justuser@example.com",
                  "password": "wrong-password"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                // ❗ 실패 케이스는 상태코드만 검증
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_fail_notFoundEmail() throws Exception {
        String body = """
                {
                  "email": "no-such-user@example.com",
                  "password": "anything"
                }
                """;

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                // ❗ 마찬가지로 상태 코드만 확인
                .andExpect(status().is4xxClientError());
    }
}
