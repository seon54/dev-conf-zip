package com.dev.conf.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {
    // OK
    OK(200, "OK"),
    CREATED(201, "Created"),

    // Client Error
    BAD_REQUEST(400, "Bad Request"),
    USER_NOT_FOUND(404, "User Not Found"),
    TOKEN_INVALID(400, "Invalid Token"),
    TOKEN_EXPIRED(400, "Expired Token"),

    CONFERENCE_ALREADY_EXISTS(400, "Conference Already Exists"),
    CONFERENCE_NOT_FOUND(404, "Conference Not Found"),

    // Server Error
    SERVER_ERROR(500, "Server Error");

    private final Integer code;
    private final String message;
}
