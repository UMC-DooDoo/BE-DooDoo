package com.umc.doodoo.domain.category.dto.request;

public record CategoryCreateRequest(
        String categoryName,
        String color
) {
}
