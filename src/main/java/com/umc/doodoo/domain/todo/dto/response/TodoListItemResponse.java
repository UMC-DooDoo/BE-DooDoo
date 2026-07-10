package com.umc.doodoo.domain.todo.dto.response;

import com.umc.doodoo.domain.todo.entity.Todo;

public record TodoListItemResponse(
        Long todoId,
        String title,
        Long categoryId,
        Integer priority,
        boolean isCompleted
) {
    public static TodoListItemResponse from(Todo todo) {
        return new TodoListItemResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getCategoryId(),
                todo.getPriority().getValue(),
                todo.isCompleted()
        );
    }
}
