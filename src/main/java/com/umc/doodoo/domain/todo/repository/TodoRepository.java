package com.umc.doodoo.domain.todo.repository;

import com.umc.doodoo.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Optional<Todo> findByIdAndUserId(Long id, Long userId);

    List<Todo> findByUserIdAndTaskDate(Long userId, LocalDate taskDate);

    List<Todo> findByUserIdAndTaskDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
