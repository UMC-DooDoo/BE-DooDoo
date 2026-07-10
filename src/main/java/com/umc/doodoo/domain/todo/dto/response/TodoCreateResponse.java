package com.umc.doodoo.domain.todo.dto.response;

import com.umc.doodoo.domain.todo.entity.Todo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TodoCreateResponse(
        Long todoId,
        Long userId,
        Long categoryId,
        String title,
        LocalDate taskDate,
        Integer priority,
        boolean isCompleted,
        LocalDateTime createdAt
) {
    public static TodoCreateResponse from(Todo todo) {
        return new TodoCreateResponse(
                todo.getId(),
                todo.getUserId(),
                todo.getCategoryId(),
                todo.getTitle(),
                todo.getTaskDate(),
                todo.getPriority().getValue(),
                todo.isCompleted(),
                todo.getCreatedAt()
        );
    }
}
