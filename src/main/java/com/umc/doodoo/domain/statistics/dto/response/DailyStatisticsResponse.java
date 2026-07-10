package com.umc.doodoo.domain.statistics.dto.response;

import java.util.List;

public record DailyStatisticsResponse(int year, int month, List<DailyStatisticItemResponse> dailyStatistics) {
}
