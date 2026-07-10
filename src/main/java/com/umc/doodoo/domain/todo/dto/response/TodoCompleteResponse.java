package com.umc.doodoo.domain.todo.dto.response;

public record TodoCompleteResponse(
        Long todoId,
        boolean isCompleted
) {
}
