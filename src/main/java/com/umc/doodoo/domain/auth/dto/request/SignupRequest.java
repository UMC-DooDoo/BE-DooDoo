package com.umc.doodoo.domain.auth.dto.request;

public record SignupRequest(
        String signupId,
        String password,
        String nickname
) {
}
