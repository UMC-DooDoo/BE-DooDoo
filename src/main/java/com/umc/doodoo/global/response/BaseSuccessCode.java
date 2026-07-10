package com.umc.doodoo.global.response;

import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {

    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
