package com.umc.doodoo.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(

        String categoryName,

        String color
) {
}
