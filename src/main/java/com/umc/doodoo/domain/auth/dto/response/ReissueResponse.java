package com.umc.doodoo.domain.auth.dto.response;

public record ReissueResponse(
        String accessToken,
        String refreshToken
) {
}
