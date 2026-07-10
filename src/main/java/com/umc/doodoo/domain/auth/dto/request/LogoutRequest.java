package com.umc.doodoo.domain.auth.dto.request;

public record LogoutRequest(
        String refreshToken
) {
}
