package com.example.bookstore.common;

import com.example.bookstore.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("헬스 체크 - OK 응답")
    void health_success() throws Exception {
        mockMvc.perform(
                        get("/health")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.status").value("OK"));
    }
}
