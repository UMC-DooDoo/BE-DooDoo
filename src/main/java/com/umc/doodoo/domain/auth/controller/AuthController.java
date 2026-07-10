package com.umc.doodoo.domain.auth.controller;

import com.umc.doodoo.domain.auth.dto.request.LoginRequest;
import com.umc.doodoo.domain.auth.dto.request.LogoutRequest;
import com.umc.doodoo.domain.auth.dto.request.ReissueRequest;
import com.umc.doodoo.domain.auth.dto.request.SignupRequest;
import com.umc.doodoo.domain.auth.dto.response.LoginResponse;
import com.umc.doodoo.domain.auth.dto.response.ReissueResponse;
import com.umc.doodoo.domain.auth.dto.response.SignupResponse;
import com.umc.doodoo.domain.auth.exception.AuthSuccessCode;
import com.umc.doodoo.domain.auth.service.AuthService;
import com.umc.doodoo.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "signupId, password, nickname으로 회원가입합니다.")
    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest request) {
        return ApiResponse.onSuccess(AuthSuccessCode.SIGNUP_SUCCESS, authService.signup(request));
    }

    @Operation(summary = "로그인", description = "signupId, password로 로그인하고 accessToken/refreshToken을 발급합니다.")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.onSuccess(AuthSuccessCode.LOGIN_SUCCESS, authService.login(request));
    }

    @Operation(summary = "로그아웃", description = "refreshToken을 무효화합니다. Authorization 헤더에 accessToken이 필요합니다.")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
        return ApiResponse.onSuccess(AuthSuccessCode.LOGOUT_SUCCESS, null);
    }

    @Operation(summary = "토큰 재발급", description = "만료된 accessToken과 refreshToken으로 새 토큰 쌍을 재발급합니다.")
    @PostMapping("/reissue")
    public ApiResponse<ReissueResponse> reissue(@RequestBody ReissueRequest request) {
        return ApiResponse.onSuccess("토큰 재발급 성공입니다.", authService.reissue(request));
    }
}
