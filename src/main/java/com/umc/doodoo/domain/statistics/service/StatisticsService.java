package com.umc.doodoo.domain.statistics.service;

import com.umc.doodoo.domain.category.entity.Category;
import com.umc.doodoo.domain.category.repository.CategoryRepository;
import com.umc.doodoo.domain.statistics.dto.response.*;
import com.umc.doodoo.domain.statistics.exception.StatisticsErrorCode;
import com.umc.doodoo.domain.todo.entity.Priority;
import com.umc.doodoo.domain.todo.entity.Todo;
import com.umc.doodoo.domain.todo.repository.TodoRepository;
import com.umc.doodoo.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    public StatisticsSummaryResponse getSummary(Long userId, Integer year, Integer month) {
        YearMonth period = validatePeriod(year, month);
        List<Todo> todos = findMonthlyTodos(userId, period);
        int totalCount = todos.size();
        int completedCount = (int) todos.stream().filter(Todo::isCompleted).count();
        int completionRate = calculateCompletionRate(completedCount, totalCount);
        Banner banner = Banner.from(completionRate);

        return new StatisticsSummaryResponse(year, month, totalCount, completedCount,
                totalCount - completedCount, completionRate, banner.title, banner.message);
    }

    public DailyStatisticsResponse getDailyStatistics(Long userId, Integer year, Integer month) {
        YearMonth period = validatePeriod(year, month);
        Map<LocalDate, List<Todo>> todosByDate = findMonthlyTodos(userId, period).stream()
                .collect(Collectors.groupingBy(Todo::getTaskDate, LinkedHashMap::new, Collectors.toList()));

        List<DailyStatisticItemResponse> items = todosByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    int completed = (int) entry.getValue().stream().filter(Todo::isCompleted).count();
                    return new DailyStatisticItemResponse(entry.getKey(), completed, entry.getValue().size() - completed);
                })
                .toList();
        return new DailyStatisticsResponse(year, month, items);
    }

    public CategoryStatisticsResponse getCategoryStatistics(Long userId, Integer year, Integer month) {
        YearMonth period = validatePeriod(year, month);
        Map<Long, List<Todo>> todosByCategory = findMonthlyTodos(userId, period).stream()
                .collect(Collectors.groupingBy(Todo::getCategoryId));
        Map<Long, Category> categoriesById = categoryRepository.findByMemberId(userId).stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));

        List<CategoryStatisticItemResponse> items = todosByCategory.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> createCategoryStatistic(categoriesById, entry.getKey(), entry.getValue()))
                .toList();
        return new CategoryStatisticsResponse(year, month, items);
    }

    public PriorityStatisticsResponse getPriorityStatistics(Long userId, Integer year, Integer month) {
        YearMonth period = validatePeriod(year, month);
        Map<Priority, List<Todo>> todosByPriority = findMonthlyTodos(userId, period).stream()
                .collect(Collectors.groupingBy(Todo::getPriority));

        List<PriorityStatisticItemResponse> items = Arrays.stream(Priority.values())
                .map(priority -> {
                    List<Todo> todos = todosByPriority.getOrDefault(priority, List.of());
                    int completed = (int) todos.stream().filter(Todo::isCompleted).count();
                    return new PriorityStatisticItemResponse(priority.getValue(), priority.getValue() + "순위",
                            completed, todos.size(), calculateCompletionRate(completed, todos.size()));
                })
                .toList();
        return new PriorityStatisticsResponse(year, month, items);
    }

    private CategoryStatisticItemResponse createCategoryStatistic(
            Map<Long, Category> categoriesById, Long categoryId, List<Todo> todos) {
        Category category = categoriesById.get(categoryId);
        if (category == null) {
            throw new IllegalStateException("Todo의 Category를 찾을 수 없습니다. categoryId=" + categoryId);
        }
        int completed = (int) todos.stream().filter(Todo::isCompleted).count();
        return new CategoryStatisticItemResponse(category.getId(), category.getCategoryName(),
                category.getColor().getValue(), completed, todos.size(),
                calculateCompletionRate(completed, todos.size()));
    }

    private List<Todo> findMonthlyTodos(Long userId, YearMonth period) {
        return todoRepository.findByUserIdAndTaskDateBetween(userId, period.atDay(1), period.atEndOfMonth());
    }

    private YearMonth validatePeriod(Integer year, Integer month) {
        if (year == null || month == null || month < 1 || month > 12) {
            throw new CustomException(StatisticsErrorCode.STATISTICS_INVALID_PERIOD);
        }
        try {
            return YearMonth.of(year, month);
        } catch (RuntimeException exception) {
            throw new CustomException(StatisticsErrorCode.STATISTICS_INVALID_PERIOD);
        }
    }

    private int calculateCompletionRate(int completed, int total) {
        return total == 0 ? 0 : (int) Math.round(completed * 100.0 / total);
    }

    private record Banner(String title, String message) {
        private static Banner from(int rate) {
            if (rate == 100) return new Banner("이번 달 목표 달성 완료!", "모든 할 일을 완료했어요. 정말 대단해요!");
            if (rate >= 75) return new Banner("목표가 눈앞이에요!", "조금만 더 하면 이번 달 목표를 달성할 수 있어요!");
            if (rate >= 50) return new Banner("절반 이상 달성!", "좋은 흐름이에요. 조금만 더 힘내봐요!");
            if (rate >= 25) return new Banner("조금씩 달성 중!", "꾸준히 하나씩 완료해 봐요!");
            return new Banner("이제 시작해 볼까요?", "첫 번째 할 일을 완료해 보세요!");
        }
    }
}
