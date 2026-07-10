package com.umc.doodoo.domain.todo.dto.request;

import java.time.LocalDate;

public record TodoUpdateRequest(
        String title,
        Long categoryId,
        LocalDate taskDate,
        Integer priority
) {
}
