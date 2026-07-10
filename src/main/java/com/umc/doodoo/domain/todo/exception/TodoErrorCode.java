package com.umc.doodoo.domain.todo.exception;

import com.umc.doodoo.global.code.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TodoErrorCode implements BaseErrorCode {

    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO404", "존재하지 않는 할일입니다."),
    TODO_INVALID_INPUT(HttpStatus.BAD_REQUEST, "TODO400", "할일 입력값을 다시 확인해 주세요."),
    CALENDAR_INVALID_INPUT(HttpStatus.BAD_REQUEST, "CALENDAR400", "캘린더 조회 값을 다시 확인해 주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    TodoErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
