package com.umc.doodoo.domain.todo.service;

import com.umc.doodoo.domain.category.repository.CategoryRepository;
import com.umc.doodoo.domain.member.repository.MemberRepository;
import com.umc.doodoo.domain.todo.dto.request.TodoCreateRequest;
import com.umc.doodoo.domain.todo.exception.TodoErrorCode;
import com.umc.doodoo.domain.todo.repository.TodoRepository;
import com.umc.doodoo.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoService = new TodoService(todoRepository, memberRepository, categoryRepository);
    }

    @Test
    void createTodoRejectsCategoryNotOwnedByMember() {
        Long memberId = 3L;
        Long categoryId = 2L;
        TodoCreateRequest request = new TodoCreateRequest(
                categoryId, "할 일", LocalDate.of(2026, 7, 10), 1);
        when(categoryRepository.existsByIdAndMemberId(categoryId, memberId)).thenReturn(false);

        assertThatThrownBy(() -> todoService.createTodo(memberId, request))
                .isInstanceOfSatisfying(CustomException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(TodoErrorCode.TODO_INVALID_INPUT));

        verifyNoInteractions(memberRepository, todoRepository);
    }
}
