package com.umc.doodoo.domain.todo.repository;

import com.umc.doodoo.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Optional<Todo> findByIdAndMemberId(Long id, Long memberId);

    List<Todo> findByMemberIdAndTaskDate(Long memberId, LocalDate taskDate);

    List<Todo> findByMemberIdAndTaskDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);
}
