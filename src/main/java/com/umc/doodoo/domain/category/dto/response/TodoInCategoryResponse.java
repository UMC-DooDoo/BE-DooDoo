package com.umc.doodoo.domain.category.dto.response;

import com.umc.doodoo.domain.todo.entity.Todo;

public record TodoInCategoryResponse(
        Long todoId,
        String title,
        Integer priority,
        boolean isCompleted
) {
    public static TodoInCategoryResponse from(Todo todo) {
        return new TodoInCategoryResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getPriority().getValue(),
                todo.isCompleted()
        );
    }
}
