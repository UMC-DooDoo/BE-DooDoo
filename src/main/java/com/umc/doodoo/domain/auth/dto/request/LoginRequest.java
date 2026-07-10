package com.umc.doodoo.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record LoginRequest(
        @NotBlank
        String signupId,
        @NotBlank
        String password
) {
}
