package com.umc.doodoo.domain.category.dto.response;

import com.umc.doodoo.domain.category.entity.Category;

public record CategoryListItemResponse(
        Long categoryId,
        String categoryName,
        String color
) {
    public static CategoryListItemResponse from(Category category) {
        return new CategoryListItemResponse(
                category.getId(),
                category.getCategoryName(),
                category.getColor().getValue()
        );
    }
}
