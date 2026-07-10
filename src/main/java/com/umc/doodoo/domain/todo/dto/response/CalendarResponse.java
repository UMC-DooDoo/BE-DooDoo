package com.umc.doodoo.domain.todo.dto.response;

import java.util.List;

public record CalendarResponse(
        String month,
        List<CalendarDayResponse> days
) {
}
