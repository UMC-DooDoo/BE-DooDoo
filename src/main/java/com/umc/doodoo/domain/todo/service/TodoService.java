package com.umc.doodoo.domain.todo.service;

import com.umc.doodoo.domain.member.entity.Member;
import com.umc.doodoo.domain.member.repository.MemberRepository;
import com.umc.doodoo.domain.todo.dto.request.TodoCreateRequest;
import com.umc.doodoo.domain.todo.dto.request.TodoUpdateRequest;
import com.umc.doodoo.domain.todo.dto.response.CalendarDayResponse;
import com.umc.doodoo.domain.todo.dto.response.CalendarResponse;
import com.umc.doodoo.domain.todo.dto.response.TodoCompleteResponse;
import com.umc.doodoo.domain.todo.dto.response.TodoCreateResponse;
import com.umc.doodoo.domain.todo.dto.response.TodoListItemResponse;
import com.umc.doodoo.domain.todo.dto.response.TodoListResponse;
import com.umc.doodoo.domain.todo.dto.response.TodoUpdateResponse;
import com.umc.doodoo.domain.todo.entity.Priority;
import com.umc.doodoo.domain.todo.entity.Todo;
import com.umc.doodoo.domain.todo.exception.TodoErrorCode;
import com.umc.doodoo.domain.todo.repository.TodoRepository;
import com.umc.doodoo.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TodoCreateResponse createTodo(Long userId, TodoCreateRequest request) {
        if (request.categoryId() == null
                || request.taskDate() == null || request.priority() == null
                || request.title() == null || request.title().isBlank()
                || request.title().length() > 30) {
            throw new CustomException(TodoErrorCode.TODO_INVALID_INPUT);
        }

        Priority priority = Priority.fromValue(request.priority());
        Member member = memberRepository.getReferenceById(userId);

        Todo todo = Todo.builder()
                .member(member)
                .categoryId(request.categoryId())
                .title(request.title())
                .taskDate(request.taskDate())
                .priority(priority)
                .build();

        return TodoCreateResponse.from(todoRepository.save(todo));
    }

    public TodoListResponse getTodosByDate(Long userId, String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new CustomException(TodoErrorCode.TODO_INVALID_INPUT);
        }

        LocalDate date = parseDate(dateStr);
        List<Todo> todos = todoRepository.findByMemberIdAndTaskDate(userId, date);

        List<TodoListItemResponse> items = todos.stream()
                .map(TodoListItemResponse::from)
                .toList();

        long completedCount = todos.stream().filter(Todo::isCompleted).count();

        return new TodoListResponse(date, todos.size(), (int) completedCount, items);
    }

    @Transactional
    public TodoUpdateResponse updateTodo(Long userId, Long todoId, TodoUpdateRequest request) {
        Todo todo = findTodoOrThrow(userId, todoId);

        if (request.title() == null && request.categoryId() == null
                && request.taskDate() == null && request.priority() == null) {
            throw new CustomException(TodoErrorCode.TODO_INVALID_INPUT);
        }

        if (request.title() != null) {
            if (request.title().isBlank() || request.title().length() > 30) {
                throw new CustomException(TodoErrorCode.TODO_INVALID_INPUT);
            }
            todo.updateTitle(request.title());
        }
        if (request.categoryId() != null) {
            todo.updateCategoryId(request.categoryId());
        }
        if (request.taskDate() != null) {
            todo.updateTaskDate(request.taskDate());
        }
        if (request.priority() != null) {
            todo.updatePriority(Priority.fromValue(request.priority()));
        }

        return TodoUpdateResponse.from(todo);
    }

    @Transactional
    public TodoCompleteResponse toggleComplete(Long userId, Long todoId) {
        Todo todo = findTodoOrThrow(userId, todoId);
        todo.toggleComplete();
        return new TodoCompleteResponse(todo.getId(), todo.isCompleted());
    }

    @Transactional
    public void deleteTodo(Long userId, Long todoId) {
        Todo todo = findTodoOrThrow(userId, todoId);
        todoRepository.delete(todo);
    }

    public CalendarResponse getCalendar(Long userId, Integer year, Integer month) {
        if (year == null || month == null) {
            throw new CustomException(TodoErrorCode.CALENDAR_INVALID_INPUT);
        }

        YearMonth yearMonth = toYearMonth(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Todo> todos = todoRepository.findByMemberIdAndTaskDateBetween(userId, startDate, endDate);

        Map<LocalDate, List<Todo>> todosByDate = todos.stream()
                .collect(Collectors.groupingBy(Todo::getTaskDate, LinkedHashMap::new, Collectors.toList()));

        List<CalendarDayResponse> days = todosByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    List<Integer> priorities = entry.getValue().stream()
                            .map(Todo::getPriority)
                            .distinct()
                            .map(Priority::getValue)
                            .toList();
                    boolean allCompleted = entry.getValue().stream().allMatch(Todo::isCompleted);
                    return new CalendarDayResponse(entry.getKey(), priorities, allCompleted);
                })
                .toList();

        return new CalendarResponse(yearMonth.toString(), days);
    }

    private Todo findTodoOrThrow(Long userId, Long todoId) {
        return todoRepository.findByIdAndMemberId(todoId, userId)
                .orElseThrow(() -> new CustomException(TodoErrorCode.TODO_NOT_FOUND));
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new CustomException(TodoErrorCode.TODO_INVALID_INPUT);
        }
    }

    private YearMonth toYearMonth(int year, int month) {
        try {
            return YearMonth.of(year, month);
        } catch (DateTimeException e) {
            throw new CustomException(TodoErrorCode.CALENDAR_INVALID_INPUT);
        }
    }
}
