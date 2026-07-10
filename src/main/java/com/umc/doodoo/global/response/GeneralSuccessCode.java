package com.umc.doodoo.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GeneralSuccessCode implements BaseSuccessCode {

    OK(HttpStatus.OK, "COMMON200_1", "요청에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    GeneralSuccessCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
