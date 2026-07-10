package com.umc.doodoo.domain.category.service;

import com.umc.doodoo.domain.category.entity.Category;
import com.umc.doodoo.domain.category.exception.CategoryErrorCode;
import com.umc.doodoo.domain.category.repository.CategoryRepository;
import com.umc.doodoo.domain.member.repository.MemberRepository;
import com.umc.doodoo.domain.todo.repository.TodoRepository;
import com.umc.doodoo.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Category category;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository, todoRepository, memberRepository);
    }

    @Test
    void deleteCategoryRejectsCategoryReferencedByTodo() {
        Long memberId = 1L;
        Long categoryId = 2L;
        when(categoryRepository.findByIdAndMemberId(categoryId, memberId)).thenReturn(Optional.of(category));
        when(todoRepository.existsByCategoryId(categoryId)).thenReturn(true);

        assertThatThrownBy(() -> categoryService.deleteCategory(memberId, categoryId))
                .isInstanceOfSatisfying(CustomException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(CategoryErrorCode.CATEGORY_IN_USE));

        verify(categoryRepository, never()).delete(category);
    }
}
