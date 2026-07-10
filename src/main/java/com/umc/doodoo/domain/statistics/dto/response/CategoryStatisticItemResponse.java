package com.umc.doodoo.domain.statistics.dto.response;

public record CategoryStatisticItemResponse(
        Long categoryId, String categoryName, String color,
        int completedCount, int totalCount, int completionRate
) {
}
