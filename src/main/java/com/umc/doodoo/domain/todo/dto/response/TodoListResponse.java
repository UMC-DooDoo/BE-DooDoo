package com.umc.doodoo.domain.todo.dto.response;

import java.time.LocalDate;
import java.util.List;

public record TodoListResponse(
        LocalDate date,
        int totalCount,
        int completedCount,
        List<TodoListItemResponse> todos
) {
}
