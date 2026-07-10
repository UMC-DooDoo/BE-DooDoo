package com.umc.doodoo.domain.todo.dto.response;

import com.umc.doodoo.domain.todo.entity.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TodoUpdateResponse(
        Long todoId,
        Long userId,
        Long categoryId,
        String title,
        LocalDate taskDate,
        Integer priority,
        boolean isCompleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TodoUpdateResponse from(Todo todo) {
        return new TodoUpdateResponse(
                todo.getId(),
                todo.getUserId(),
                todo.getCategoryId(),
                todo.getTitle(),
                todo.getTaskDate(),
                todo.getPriority().getValue(),
                todo.isCompleted(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );
    }
}
