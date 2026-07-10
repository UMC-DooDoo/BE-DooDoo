package com.umc.doodoo.domain.category.dto.response;

import com.umc.doodoo.domain.category.entity.Category;
import com.umc.doodoo.domain.todo.entity.Todo;

public record TodoInPriorityResponse(
        Long todoId,
        String title,
        boolean isCompleted,
        Long categoryId,
        String categoryName,
        String color
) {
    public static TodoInPriorityResponse from(Todo todo, Category category) {
        return new TodoInPriorityResponse(
                todo.getId(),
                todo.getTitle(),
                todo.isCompleted(),
                todo.getCategoryId(),
                category != null ? category.getCategoryName() : null,
                category != null ? category.getColor().getValue() : null
        );
    }
}
