package com.umc.doodoo.global.code;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
