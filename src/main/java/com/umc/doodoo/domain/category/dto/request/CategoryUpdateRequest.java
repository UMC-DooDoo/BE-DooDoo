package com.umc.doodoo.domain.category.dto.request;

public record CategoryUpdateRequest(
        String categoryName,
        String color
) {
}
