package com.umc.doodoo.domain.statistics.dto.response;

public record StatisticsSummaryResponse(
        int year,
        int month,
        int totalCount,
        int completedCount,
        int incompleteCount,
        int completionRate,
        String bannerTitle,
        String bannerMessage
) {
}
