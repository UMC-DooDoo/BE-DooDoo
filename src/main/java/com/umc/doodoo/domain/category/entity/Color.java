package com.umc.doodoo.domain.category.entity;

import com.umc.doodoo.domain.category.exception.CategoryErrorCode;
import com.umc.doodoo.global.exception.CustomException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Color {

    BLUE("BLUE", "#1240D9"),
    GREEN("GREEN", "#29C878"),
    ORANGE("ORANGE", "#DC710E"),
    PURPLE("PURPLE", "#7C2BE5"),
    PINK("PINK", "#DC0E7C"),
    CYAN("CYAN", "#2BC0E5"),
    YELLOW("YELLOW", "#969B06"),
    RED("RED", "#FF3241");

    private final String value;
    private final String hexCode;

    Color(String value, String hexCode) {
        this.value = value;
        this.hexCode = hexCode;
    }

    public static Color fromValue(String value) {
        return Arrays.stream(values())
                .filter(c -> c.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new CustomException(CategoryErrorCode.CATEGORY_INVALID_INPUT));
    }
}
