package com.umc.doodoo.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    COMMON400(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    COMMON500(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 오류가 발생했습니다."),

    USER404(HttpStatus.NOT_FOUND, "USER404", "존재하지 않는 사용자입니다."),

    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO404", "존재하지 않는 할일입니다."),
    TODO_INVALID_INPUT(HttpStatus.BAD_REQUEST, "TODO400", "할일 입력값을 다시 확인해 주세요."),

    CALENDAR_INVALID_INPUT(HttpStatus.BAD_REQUEST, "CALENDAR400", "캘린더 조회 값을 다시 확인해 주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
