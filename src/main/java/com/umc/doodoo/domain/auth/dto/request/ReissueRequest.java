package com.umc.doodoo.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReissueRequest(
        @NotBlank
        String accessToken,
        @NotBlank
        String refreshToken
) {
}
