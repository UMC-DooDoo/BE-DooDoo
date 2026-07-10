package com.umc.doodoo.domain.statistics.controller;

import com.umc.doodoo.domain.statistics.dto.response.CategoryStatisticsResponse;
import com.umc.doodoo.domain.statistics.dto.response.DailyStatisticsResponse;
import com.umc.doodoo.domain.statistics.dto.response.PriorityStatisticsResponse;
import com.umc.doodoo.domain.statistics.dto.response.StatisticsSummaryResponse;
import com.umc.doodoo.domain.statistics.service.StatisticsService;
import com.umc.doodoo.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Statistics", description = "통계 조회 API")
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Operation(summary = "월별 통계 조회")
    @GetMapping("/summary")
    public ApiResponse<StatisticsSummaryResponse> getSummary(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        return ApiResponse.onSuccess(statisticsService.getSummary(userId, year, month));
    }

    @Operation(summary = "일별 달성 현황 조회")
    @GetMapping("/daily")
    public ApiResponse<DailyStatisticsResponse> getDailyStatistics(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        return ApiResponse.onSuccess(statisticsService.getDailyStatistics(userId, year, month));
    }

    @Operation(summary = "분야별 달성률 조회")
    @GetMapping("/category")
    public ApiResponse<CategoryStatisticsResponse> getCategoryStatistics(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        return ApiResponse.onSuccess(statisticsService.getCategoryStatistics(userId, year, month));
    }

    @Operation(summary = "우선순위별 달성률 조회")
    @GetMapping("/priority")
    public ApiResponse<PriorityStatisticsResponse> getPriorityStatistics(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        return ApiResponse.onSuccess(statisticsService.getPriorityStatistics(userId, year, month));
    }
}
