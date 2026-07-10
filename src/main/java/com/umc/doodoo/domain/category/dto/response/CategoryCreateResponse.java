package com.umc.doodoo.domain.category.dto.response;

import com.umc.doodoo.domain.category.entity.Category;

import java.time.LocalDateTime;

public record CategoryCreateResponse(
        Long categoryId,
        String categoryName,
        String color,
        LocalDateTime createdAt
) {
    public static CategoryCreateResponse from(Category category) {
        return new CategoryCreateResponse(
                category.getId(),
                category.getCategoryName(),
                category.getColor().getValue(),
                category.getCreatedAt()
        );
    }
}
