package com.umc.doodoo.domain.todo.entity;

import com.umc.doodoo.domain.todo.exception.TodoErrorCode;
import com.umc.doodoo.global.exception.CustomException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Priority {

    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4);

    private final int value;

    Priority(int value) {
        this.value = value;
    }

    public static Priority fromValue(int value) {
        return Arrays.stream(values())
                .filter(priority -> priority.value == value)
                .findFirst()
                .orElseThrow(() -> new CustomException(TodoErrorCode.TODO_INVALID_INPUT));
    }
}
