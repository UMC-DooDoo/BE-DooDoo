package com.umc.doodoo.domain.statistics.exception;

import com.umc.doodoo.global.code.BaseErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatisticsErrorCode implements BaseErrorCode {

    STATISTICS_INVALID_PERIOD(
            HttpStatus.BAD_REQUEST,
            "STATISTICS_INVALID_PERIOD",
            "통계 조회 기간을 다시 확인해 주세요."
    );

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    StatisticsErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
