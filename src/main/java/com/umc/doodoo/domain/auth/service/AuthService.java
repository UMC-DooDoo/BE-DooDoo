package com.umc.doodoo.domain.auth.service;

import com.umc.doodoo.domain.auth.dto.request.LoginRequest;
import com.umc.doodoo.domain.auth.dto.request.LogoutRequest;
import com.umc.doodoo.domain.auth.dto.request.ReissueRequest;
import com.umc.doodoo.domain.auth.dto.request.SignupRequest;
import com.umc.doodoo.domain.auth.dto.response.LoginResponse;
import com.umc.doodoo.domain.auth.dto.response.ReissueResponse;
import com.umc.doodoo.domain.auth.dto.response.SignupResponse;
import com.umc.doodoo.domain.auth.entity.RefreshToken;
import com.umc.doodoo.domain.auth.exception.AuthErrorCode;
import com.umc.doodoo.domain.auth.repository.RefreshTokenRepository;
import com.umc.doodoo.domain.member.entity.Member;
import com.umc.doodoo.domain.member.repository.MemberRepository;
import com.umc.doodoo.global.code.GeneralErrorCode;
import com.umc.doodoo.global.exception.CustomException;
import com.umc.doodoo.global.security.jwt.JwtProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (request.signupId() == null || request.signupId().isBlank()
                || request.password() == null || request.password().isBlank()
                || request.nickname() == null || request.nickname().isBlank()) {
            throw new CustomException(GeneralErrorCode.BAD_REQUEST);
        }

        if (memberRepository.existsBySignupId(request.signupId())) {
            throw new CustomException(AuthErrorCode.AUTH_DUPLICATE_ID);
        }

        Member member = Member.builder()
                .signupId(request.signupId())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .build();

        Member saved = memberRepository.save(member);
        return new SignupResponse(saved.getId());
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findBySignupId(request.signupId())
                .orElseThrow(() -> new CustomException(AuthErrorCode.AUTH_LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(AuthErrorCode.AUTH_LOGIN_FAILED);
        }

        String accessToken = jwtProvider.createAccessToken(member.getId());
        String refreshToken = jwtProvider.createRefreshToken(member.getId());

        saveRefreshToken(member.getId(), refreshToken);

        return new LoginResponse(member.getId(), accessToken, refreshToken, false, false);
    }

    @Transactional
    public void logout(LogoutRequest request) {
        if (request.refreshToken() == null || request.refreshToken().isBlank()) {
            throw new CustomException(AuthErrorCode.AUTH_LOGOUT_INVALID);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new CustomException(AuthErrorCode.AUTH_LOGOUT_INVALID));

        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public ReissueResponse reissue(ReissueRequest request) {
        if (request.accessToken() == null || request.accessToken().isBlank()
                || request.refreshToken() == null || request.refreshToken().isBlank()) {
            throw new CustomException(AuthErrorCode.AUTH_REFRESH_INVALID);
        }

        RefreshToken savedRefreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new CustomException(AuthErrorCode.AUTH_REFRESH_INVALID));

        if (savedRefreshToken.isExpired()) {
            refreshTokenRepository.delete(savedRefreshToken);
            throw new CustomException(AuthErrorCode.AUTH_TOKEN_EXPIRED);
        }

        Long memberIdFromAccessToken;
        try {
            memberIdFromAccessToken = jwtProvider.getMemberIdIgnoringExpiration(request.accessToken());
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(AuthErrorCode.AUTH_REFRESH_INVALID);
        }

        if (!memberIdFromAccessToken.equals(savedRefreshToken.getMemberId())) {
            throw new CustomException(AuthErrorCode.AUTH_REFRESH_INVALID);
        }

        String newAccessToken = jwtProvider.createAccessToken(memberIdFromAccessToken);
        String newRefreshToken = jwtProvider.createRefreshToken(memberIdFromAccessToken);

        refreshTokenRepository.delete(savedRefreshToken);
        saveRefreshToken(memberIdFromAccessToken, newRefreshToken);

        return new ReissueResponse(newAccessToken, newRefreshToken);
    }

    private void saveRefreshToken(Long memberId, String refreshToken) {
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtProvider.getRefreshTokenValidity() / 1000);
        refreshTokenRepository.save(RefreshToken.builder()
                .memberId(memberId)
                .token(refreshToken)
                .expiresAt(expiresAt)
                .build());
    }
}
