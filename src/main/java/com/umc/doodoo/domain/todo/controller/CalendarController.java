package com.umc.doodoo.domain.todo.controller;

import com.umc.doodoo.domain.todo.dto.response.CalendarResponse;
import com.umc.doodoo.domain.todo.service.TodoService;
import com.umc.doodoo.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Calendar", description = "캘린더 요약 API")
@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final TodoService todoService;

    @Operation(summary = "월별 캘린더 요약 조회", description = "인증된 사용자의 지정한 연도/월 날짜별 우선순위 목록 및 완료 여부 요약을 조회합니다.")
    @GetMapping
    public ApiResponse<CalendarResponse> getCalendar(
            @AuthenticationPrincipal Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        return ApiResponse.onSuccess(todoService.getCalendar(userId, year, month));
    }
}
