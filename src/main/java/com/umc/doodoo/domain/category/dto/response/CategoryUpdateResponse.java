package com.umc.doodoo.domain.category.dto.response;

import com.umc.doodoo.domain.category.entity.Category;

import java.time.LocalDateTime;

public record CategoryUpdateResponse(
        Long categoryId,
        Long memberId,
        String categoryName,
        String color,
        LocalDateTime updatedAt
) {
    public static CategoryUpdateResponse from(Category category) {
        return new CategoryUpdateResponse(
                category.getId(),
                category.getMemberId(),
                category.getCategoryName(),
                category.getColor().getValue(),
                category.getUpdatedAt()
        );
    }
}
