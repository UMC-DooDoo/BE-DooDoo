package com.umc.doodoo.global.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GeneralErrorCode implements BaseErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400_1", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401_1", "인증되지 않았습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403_1", "접근이 금지되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500_1", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    GeneralErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
