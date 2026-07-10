package com.umc.doodoo.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record CategoryCreateRequest(
        @NonNull
        Long memberId,

        @NotBlank
        String categoryName,

        @NotBlank
        String color
) {
}
