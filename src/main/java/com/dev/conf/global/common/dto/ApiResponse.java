package com.dev.conf.global.common.dto;

import com.dev.conf.global.common.enums.StatusCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public ApiResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    public ApiResponse(StatusCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse<Object> success() {
        return new ApiResponse<>(StatusCode.OK);
    }

    public static ApiResponse<Object> success(Object data) {
        return new ApiResponse<>(StatusCode.OK, data);
    }

    public static ApiResponse<Object> badRequest() {
        return new ApiResponse<>(StatusCode.BAD_REQUEST);
    }

    public static ApiResponse<Object> of(StatusCode statusCode) {
        return new ApiResponse<>(statusCode);
    }
}
