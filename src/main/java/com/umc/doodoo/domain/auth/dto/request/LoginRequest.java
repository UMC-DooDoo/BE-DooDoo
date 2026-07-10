package com.umc.doodoo.domain.auth.dto.request;

public record LoginRequest(
        String signupId,
        String password
) {
}
