package com.umc.doodoo.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

import java.time.LocalDate;

public record TodoUpdateRequest(
        @NonNull
        String title,

        @NonNull
        @Size(min = 1)
        Long categoryId,

        @NotBlank
        LocalDate taskDate,

        @NonNull
        @Size(min = 1, max = 4)
        Integer priority
) {
}
