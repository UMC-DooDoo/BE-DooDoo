package com.umc.doodoo.domain.todo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TodoCreateRequest(
        @NotNull
        Long categoryId,

        @NotBlank
        @Size(max = 30)
        String title,

        @NotNull
        LocalDate taskDate,

        @NotNull
        @Min(1)
        @Max(4)
        Integer priority
) {
}
