package com.umc.doodoo.domain.member.repository;

import com.umc.doodoo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsBySignupId(String signupId);

    Optional<Member> findBySignupId(String signupId);
}
