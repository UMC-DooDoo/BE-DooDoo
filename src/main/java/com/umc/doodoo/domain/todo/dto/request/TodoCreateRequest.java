package com.umc.doodoo.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

import java.time.LocalDate;

public record TodoCreateRequest(
        @NonNull
        @Size(min = 1)
        Long categoryId,

        @NotBlank
        String title,

        @NotBlank
        LocalDate taskDate,

        @NonNull
        @Size(min = 1, max = 4)
        Integer priority
) {
}
