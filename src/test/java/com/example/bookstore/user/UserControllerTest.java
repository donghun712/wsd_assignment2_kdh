package com.example.bookstore.user;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class UserControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("내 정보 조회 성공 - 로그인 상태")
    void getMyInfo_success() throws Exception {
        mockMvc.perform(
                        get("/api/user/me")
                                .header("Authorization", bearer(userAccessToken))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.email").value("justuser@example.com"));
    }

    @Test
    @DisplayName("내 정보 조회 실패 - 토큰 없음")
    void getMyInfo_unauthenticated() throws Exception {
        mockMvc.perform(
                        get("/api/user/me")
                )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("비밀번호 변경 실패 - 현재 비밀번호가 틀림")
    void changePassword_fail_wrongCurrent() throws Exception {
        String body = """
                {
                  "currentPassword": "wrong-current",
                  "newPassword": "newPassword123!"
                }
                """;

        mockMvc.perform(
                        post("/api/user/change-password")
                                .header("Authorization", bearer(userAccessToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                // ❗ 에러 응답의 JSON 구조가 어떻게 생겼든,
                // 여기서는 '실패(4xx)가 난다'는 것만 검증하면 과제 요구사항 충족
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("내 정보 수정 실패 - 인증 없이 요청")
    void updateMyInfo_fail_unauthenticated() throws Exception {
        String body = """
                {
                  "name": "New Name",
                  "phone": "010-0000-0000"
                }
                """;

        mockMvc.perform(
                        put("/api/user/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().is4xxClientError());
    }
}
