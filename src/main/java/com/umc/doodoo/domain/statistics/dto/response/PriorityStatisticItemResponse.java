package com.umc.doodoo.domain.statistics.dto.response;

public record PriorityStatisticItemResponse(
        int priority, String priorityName, int completedCount, int totalCount, int completionRate
) {
}
