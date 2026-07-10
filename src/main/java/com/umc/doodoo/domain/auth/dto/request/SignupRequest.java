package com.umc.doodoo.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank
        @Size(min = 4)
        String signupId,

        @NotBlank
        @Size(min = 8, max = 20)
        String password,

        @NotBlank
        @Size(min = 1, max = 12)
        String nickname
) {
}
