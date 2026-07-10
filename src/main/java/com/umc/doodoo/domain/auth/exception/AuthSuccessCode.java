package com.umc.doodoo.domain.auth.exception;

import com.umc.doodoo.global.code.BaseSuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthSuccessCode implements BaseSuccessCode {

    SIGNUP_SUCCESS(HttpStatus.OK, "AUTH200", "회원가입에 성공했습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "AUTH200", "로그인에 성공했습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "AUTH200", "로그아웃 되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    AuthSuccessCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
