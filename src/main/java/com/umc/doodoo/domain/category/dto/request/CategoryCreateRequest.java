package com.umc.doodoo.domain.category.dto.request;

public record CategoryCreateRequest(
        Long memberId,
        String categoryName,
        String color
) {
}
