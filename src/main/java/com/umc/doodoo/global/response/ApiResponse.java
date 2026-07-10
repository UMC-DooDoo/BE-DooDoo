package com.umc.doodoo.global.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.umc.doodoo.global.code.BaseErrorCode;
import com.umc.doodoo.global.code.BaseSuccessCode;
import com.umc.doodoo.global.code.GeneralSuccessCode;

@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    private final boolean success;
    private final String code;
    private final String message;
    private final T result;

    private ApiResponse(boolean success, String code, String message, T result) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.result = result;
    }

    @JsonProperty("isSuccess")
    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getResult() {
        return result;
    }

    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>(true, GeneralSuccessCode.OK.getCode(), GeneralSuccessCode.OK.getMessage(), result);
    }

    public static <T> ApiResponse<T> onSuccess(BaseSuccessCode successCode, T result) {
        return new ApiResponse<>(true, successCode.getCode(), successCode.getMessage(), result);
    }

    public static <T> ApiResponse<T> onSuccess(String message, T result) {
        return new ApiResponse<>(true, GeneralSuccessCode.OK.getCode(), message, result);
    }

    public static <T> ApiResponse<T> onFailure(BaseErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
    }
}
