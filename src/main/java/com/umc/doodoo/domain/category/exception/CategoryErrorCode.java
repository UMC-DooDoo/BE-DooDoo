package com.umc.doodoo.domain.category.exception;

import com.umc.doodoo.global.exception.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CategoryErrorCode implements BaseErrorCode {

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY404", "존재하지 않는 분야입니다."),
    CATEGORY_INVALID_INPUT(HttpStatus.BAD_REQUEST, "CATEGORY400", "분야 입력값을 다시 확인해 주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CategoryErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
