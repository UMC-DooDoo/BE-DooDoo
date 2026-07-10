package com.umc.doodoo.domain.auth.dto.request;

public record ReissueRequest(
        String accessToken,
        String refreshToken
) {
}
