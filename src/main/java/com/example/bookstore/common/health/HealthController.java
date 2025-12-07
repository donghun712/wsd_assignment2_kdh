package com.example.bookstore.common.health;

import com.example.bookstore.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Tag(name = "Health API", description = "서버 헬스 체크용 엔드포인트")
public class HealthController {

    @GetMapping("/health")
    @Operation(
            summary = "헬스 체크",
            description = "서버의 동작 상태와 현재 시간을 반환합니다. JCloud 배포 상태 확인용."
    )
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> payload = Map.of(
                "status", "OK",
                "time", LocalDateTime.now().toString()
        );
        return ApiResponse.success(payload);
    }
}
