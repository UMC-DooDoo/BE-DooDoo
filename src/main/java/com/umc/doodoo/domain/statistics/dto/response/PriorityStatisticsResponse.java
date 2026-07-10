package com.umc.doodoo.domain.statistics.dto.response;

import java.util.List;

public record PriorityStatisticsResponse(int year, int month, List<PriorityStatisticItemResponse> priorityStatistics) {
}
