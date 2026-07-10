package com.umc.doodoo.domain.statistics.dto.response;

import java.time.LocalDate;

public record DailyStatisticItemResponse(LocalDate date, int completedCount, int incompleteCount) {
}
