package com.umc.doodoo.domain.member.service;

import com.umc.doodoo.domain.member.dto.response.MemberMeResponse;
import com.umc.doodoo.domain.member.entity.Member;
import com.umc.doodoo.domain.member.exception.MemberErrorCode;
import com.umc.doodoo.domain.member.repository.MemberRepository;
import com.umc.doodoo.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberMeResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        return new MemberMeResponse(member.getId(), member.getNickname());
    }
}
