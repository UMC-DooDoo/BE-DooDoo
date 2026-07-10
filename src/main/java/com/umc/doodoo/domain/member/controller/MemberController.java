package com.umc.doodoo.domain.member.controller;

import com.umc.doodoo.domain.member.dto.response.MemberMeResponse;
import com.umc.doodoo.domain.member.service.MemberService;
import com.umc.doodoo.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "회원 정보 API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "내 정보 조회", description = "Authorization 헤더의 accessToken으로 내 정보를 조회합니다.")
    @GetMapping("/me")
    public ApiResponse<MemberMeResponse> getMyInfo(@AuthenticationPrincipal Long memberId) {
        return ApiResponse.onSuccess("성공입니다.", memberService.getMyInfo(memberId));
    }
}
