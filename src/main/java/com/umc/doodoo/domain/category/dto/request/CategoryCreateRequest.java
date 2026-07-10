package com.umc.doodoo.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryCreateRequest(
        @NotNull
        Long memberId,

        @NotBlank
        String categoryName,

        @NotBlank
        String color
) {
}
