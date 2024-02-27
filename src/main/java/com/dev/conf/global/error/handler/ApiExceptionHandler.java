package com.dev.conf.global.error.handler;

import com.dev.conf.global.common.dto.ApiResponse;
import com.dev.conf.global.error.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> general(GeneralException e) {
        return ResponseEntity.badRequest().body(ApiResponse.badRequest());
    }

}
