package com.umc.doodoo.domain.todo.controller;

import com.umc.doodoo.domain.todo.dto.request.TodoCreateRequest;
import com.umc.doodoo.domain.todo.dto.request.TodoUpdateRequest;
import com.umc.doodoo.domain.todo.dto.response.TodoCompleteResponse;
import com.umc.doodoo.domain.todo.dto.response.TodoCreateResponse;
import com.umc.doodoo.domain.todo.dto.response.TodoListResponse;
import com.umc.doodoo.domain.todo.dto.response.TodoUpdateResponse;
import com.umc.doodoo.domain.todo.service.TodoService;
import com.umc.doodoo.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Todo", description = "할일 관리 API")
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@Validated
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "할일 등록", description = "새로운 할일을 등록합니다.")
    @PostMapping
    public ApiResponse<TodoCreateResponse> createTodo(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid TodoCreateRequest request
    ) {
        return ApiResponse.onSuccess(todoService.createTodo(userId, request));
    }

    @Operation(summary = "날짜별 할일 목록 조회", description = "인증된 사용자의 특정 날짜 할일 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<TodoListResponse> getTodos(
            @AuthenticationPrincipal Long userId,
            @RequestParam @NotBlank String date
    ) {
        return ApiResponse.onSuccess(todoService.getTodosByDate(userId, date));
    }

    @Operation(summary = "할일 수정", description = "할일의 일부 필드(title, categoryId, taskDate, priority)를 부분 수정합니다.")
    @PatchMapping("/{todoId}")
    public ApiResponse<TodoUpdateResponse> updateTodo(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long todoId,
            @RequestBody TodoUpdateRequest request
    ) {
        return ApiResponse.onSuccess(todoService.updateTodo(userId, todoId, request));
    }

    @Operation(summary = "할일 완료 상태 토글", description = "할일의 완료 여부를 반전시킵니다.")
    @PatchMapping("/{todoId}/complete")
    public ApiResponse<TodoCompleteResponse> toggleComplete(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long todoId
    ) {
        return ApiResponse.onSuccess(todoService.toggleComplete(userId, todoId));
    }

    @Operation(summary = "할일 삭제", description = "할일을 삭제합니다.")
    @DeleteMapping("/{todoId}")
    public ApiResponse<Void> deleteTodo(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long todoId
    ) {
        todoService.deleteTodo(userId, todoId);
        return ApiResponse.onSuccess("할일이 삭제되었습니다.", null);
    }
}
