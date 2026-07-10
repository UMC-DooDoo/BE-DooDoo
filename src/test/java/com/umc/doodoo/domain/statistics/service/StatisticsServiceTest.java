package com.umc.doodoo.domain.statistics.service;

import com.umc.doodoo.domain.category.entity.Category;
import com.umc.doodoo.domain.category.entity.Color;
import com.umc.doodoo.domain.category.repository.CategoryRepository;
import com.umc.doodoo.domain.member.entity.Member;
import com.umc.doodoo.domain.statistics.dto.response.CategoryStatisticsResponse;
import com.umc.doodoo.domain.statistics.dto.response.DailyStatisticsResponse;
import com.umc.doodoo.domain.statistics.dto.response.PriorityStatisticsResponse;
import com.umc.doodoo.domain.statistics.dto.response.StatisticsSummaryResponse;
import com.umc.doodoo.domain.statistics.exception.StatisticsErrorCode;
import com.umc.doodoo.domain.todo.entity.Priority;
import com.umc.doodoo.domain.todo.entity.Todo;
import com.umc.doodoo.domain.todo.repository.TodoRepository;
import com.umc.doodoo.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    private static final Long USER_ID = 1L;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Member member;

    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsService(todoRepository, categoryRepository);
    }

    @Test
    void summaryCountsTodosAndReturnsBanner() {
        when(todoRepository.findByMemberIdAndTaskDateBetween(
                USER_ID, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30)))
                .thenReturn(List.of(
                        todo(10L, LocalDate.of(2026, 4, 5), Priority.FIRST, true),
                        todo(10L, LocalDate.of(2026, 4, 6), Priority.SECOND, true),
                        todo(11L, LocalDate.of(2026, 4, 7), Priority.THIRD, false)
                ));

        StatisticsSummaryResponse response = statisticsService.getSummary(USER_ID, 2026, 4);

        assertThat(response.totalCount()).isEqualTo(3);
        assertThat(response.completedCount()).isEqualTo(2);
        assertThat(response.incompleteCount()).isEqualTo(1);
        assertThat(response.completionRate()).isEqualTo(67);
        assertThat(response.bannerTitle()).isEqualTo("절반 이상 달성!");
    }

    @Test
    void summaryWithNoTodosReturnsZeroRateAndStartBanner() {
        mockEmptyMonth();

        StatisticsSummaryResponse response = statisticsService.getSummary(USER_ID, 2026, 4);

        assertThat(response.totalCount()).isZero();
        assertThat(response.completionRate()).isZero();
        assertThat(response.bannerTitle()).isEqualTo("이제 시작해 볼까요?");
    }

    @Test
    void dailyStatisticsOnlyContainsDatesWithTodosAndIsSorted() {
        when(todoRepository.findByMemberIdAndTaskDateBetween(
                USER_ID, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30)))
                .thenReturn(List.of(
                        todo(10L, LocalDate.of(2026, 4, 10), Priority.FIRST, true),
                        todo(10L, LocalDate.of(2026, 4, 5), Priority.SECOND, true),
                        todo(10L, LocalDate.of(2026, 4, 5), Priority.THIRD, false)
                ));

        DailyStatisticsResponse response = statisticsService.getDailyStatistics(USER_ID, 2026, 4);

        assertThat(response.dailyStatistics()).hasSize(2);
        assertThat(response.dailyStatistics().get(0).date()).isEqualTo(LocalDate.of(2026, 4, 5));
        assertThat(response.dailyStatistics().get(0).completedCount()).isEqualTo(1);
        assertThat(response.dailyStatistics().get(0).incompleteCount()).isEqualTo(1);
        assertThat(response.dailyStatistics().get(1).date()).isEqualTo(LocalDate.of(2026, 4, 10));
    }

    @Test
    void categoryStatisticsGroupsOnlyCategoriesWithMonthlyTodos() {
        Category category = org.mockito.Mockito.mock(Category.class);
        when(category.getId()).thenReturn(10L);
        when(category.getCategoryName()).thenReturn("공부");
        when(category.getColor()).thenReturn(Color.GREEN);
        when(categoryRepository.findByMemberId(USER_ID)).thenReturn(List.of(category));
        when(todoRepository.findByMemberIdAndTaskDateBetween(
                USER_ID, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30)))
                .thenReturn(List.of(
                        todo(10L, LocalDate.of(2026, 4, 5), Priority.FIRST, true),
                        todo(10L, LocalDate.of(2026, 4, 6), Priority.SECOND, false)
                ));

        CategoryStatisticsResponse response = statisticsService.getCategoryStatistics(USER_ID, 2026, 4);

        assertThat(response.categoryStatistics()).hasSize(1);
        assertThat(response.categoryStatistics().get(0).categoryName()).isEqualTo("공부");
        assertThat(response.categoryStatistics().get(0).color()).isEqualTo("GREEN");
        assertThat(response.categoryStatistics().get(0).completedCount()).isEqualTo(1);
        assertThat(response.categoryStatistics().get(0).totalCount()).isEqualTo(2);
        assertThat(response.categoryStatistics().get(0).completionRate()).isEqualTo(50);
    }

    @Test
    void missingCategoryIsExcludedWithoutFailingStatistics() {
        when(categoryRepository.findByMemberId(USER_ID)).thenReturn(List.of());
        when(todoRepository.findByMemberIdAndTaskDateBetween(
                USER_ID, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30)))
                .thenReturn(List.of(todo(999L, LocalDate.of(2026, 4, 5), Priority.FIRST, true)));

        CategoryStatisticsResponse response = statisticsService.getCategoryStatistics(USER_ID, 2026, 4);

        assertThat(response.categoryStatistics()).isEmpty();
    }

    @Test
    void priorityStatisticsAlwaysReturnsAllFourPriorities() {
        when(todoRepository.findByMemberIdAndTaskDateBetween(
                USER_ID, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30)))
                .thenReturn(List.of(todo(10L, LocalDate.of(2026, 4, 5), Priority.FIRST, true)));

        PriorityStatisticsResponse response = statisticsService.getPriorityStatistics(USER_ID, 2026, 4);

        assertThat(response.priorityStatistics()).hasSize(4);
        assertThat(response.priorityStatistics()).extracting(item -> item.priority())
                .containsExactly(1, 2, 3, 4);
        assertThat(response.priorityStatistics()).extracting(item -> item.priorityName())
                .containsExactly("1순위", "2순위", "3순위", "4순위");
        assertThat(response.priorityStatistics().get(0).completionRate()).isEqualTo(100);
        assertThat(response.priorityStatistics().get(1).totalCount()).isZero();
        assertThat(response.priorityStatistics().get(1).completionRate()).isZero();
    }

    @Test
    void invalidPeriodsThrowStatisticsInvalidPeriod() {
        assertInvalidPeriod(null, 4);
        assertInvalidPeriod(2026, null);
        assertInvalidPeriod(2026, 0);
        assertInvalidPeriod(2026, 13);
    }

    private void assertInvalidPeriod(Integer year, Integer month) {
        assertThatThrownBy(() -> statisticsService.getSummary(USER_ID, year, month))
                .isInstanceOfSatisfying(CustomException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(StatisticsErrorCode.STATISTICS_INVALID_PERIOD));
    }

    private void mockEmptyMonth() {
        when(todoRepository.findByMemberIdAndTaskDateBetween(
                USER_ID, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30)))
                .thenReturn(List.of());
    }

    private Todo todo(Long categoryId, LocalDate date, Priority priority, boolean completed) {
        Todo todo = Todo.builder()
                .member(member)
                .categoryId(categoryId)
                .title("할 일")
                .taskDate(date)
                .priority(priority)
                .build();
        if (completed) {
            todo.toggleComplete();
        }
        return todo;
    }
}
