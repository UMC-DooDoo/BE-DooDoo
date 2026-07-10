package com.umc.doodoo.domain.category.entity;

import com.umc.doodoo.domain.category.exception.CategoryErrorCode;
import com.umc.doodoo.global.exception.CustomException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum
    Color {

    GREEN("GREEN"),
    PINK("PINK"),
    ORANGE("ORANGE"),
    YELLOW("YELLOW"),
    PURPLE("PURPLE"),
    SKYBLUE("SKYBLUE");

    private final String value;

    Color(String value) {
        this.value = value;
    }

    public static Color fromValue(String value) {
        return Arrays.stream(values())
                .filter(c -> c.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_INVALID_INPUT));
    }
}
