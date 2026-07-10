package com.umc.doodoo.domain.auth.dto.response;

public record LoginResponse(
        Long memberId,
        String accessToken,
        String refreshToken,
        boolean isNewMember,
        boolean onboardingCompleted
) {
}
