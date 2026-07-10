package com.umc.doodoo.domain.category.dto.response;

import com.umc.doodoo.domain.category.entity.Category;

import java.util.List;

public record CategoryWithTodosResponse(
        Long categoryId,
        String categoryName,
        String color,
        List<TodoInCategoryResponse> todos
) {
    public static CategoryWithTodosResponse from(Category category, List<TodoInCategoryResponse> todos) {
        return new CategoryWithTodosResponse(
                category.getId(),
                category.getCategoryName(),
                category.getColor().getValue(),
                todos
        );
    }
}
