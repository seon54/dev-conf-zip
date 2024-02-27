package com.dev.conf.domain.user.exception;

import com.dev.conf.global.common.enums.StatusCode;
import com.dev.conf.global.error.exception.GeneralException;

public class TokenInvalidException extends GeneralException {

    public TokenInvalidException() {
        super(StatusCode.TOKEN_INVALID);
    }
}
