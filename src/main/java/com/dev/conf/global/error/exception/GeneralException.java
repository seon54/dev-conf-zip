package com.dev.conf.global.error.exception;

import com.dev.conf.global.common.enums.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class GeneralException extends RuntimeException {

    private final StatusCode statusCode;

}
