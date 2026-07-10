package com.umc.doodoo.domain.todo.dto.request;

import java.time.LocalDate;

public record TodoCreateRequest(
        Long categoryId,
        String title,
        LocalDate taskDate,
        Integer priority
) {
}
