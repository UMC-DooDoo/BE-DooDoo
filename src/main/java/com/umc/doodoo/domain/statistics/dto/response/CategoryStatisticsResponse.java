package com.umc.doodoo.domain.statistics.dto.response;

import java.util.List;

public record CategoryStatisticsResponse(int year, int month, List<CategoryStatisticItemResponse> categoryStatistics) {
}
