package com.umc.doodoo.domain.todo.dto.response;

import java.time.LocalDate;
import java.util.List;

public record CalendarDayResponse(
        LocalDate date,
        List<Integer> priorities,
        boolean allCompleted
) {
}
