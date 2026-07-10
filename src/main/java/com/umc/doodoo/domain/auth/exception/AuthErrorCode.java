package com.umc.doodoo.domain.auth.exception;

import com.umc.doodoo.global.code.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements BaseErrorCode {

    AUTH_LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH401", "아이디 또는 비밀번호가 일치하지 않습니다."),
    AUTH_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH401", "인증이 필요합니다."),
    AUTH_DUPLICATE_ID(HttpStatus.UNAUTHORIZED, "AUTH401", "이미 사용 중인 아이디입니다."),
    AUTH_LOGOUT_INVALID(HttpStatus.BAD_REQUEST, "AUTH400", "잘못된 로그아웃 요청이거나 이미 로그아웃된 계정입니다."),
    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH401", "인증 정보가 만료되었습니다. 다시 로그인해주세요."),
    AUTH_REFRESH_INVALID(HttpStatus.UNAUTHORIZED, "AUTH401", "유효하지 않은 리프레시 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    AuthErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
