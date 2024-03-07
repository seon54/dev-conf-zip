package com.dev.conf.domain.user.exception;

import com.dev.conf.global.common.enums.StatusCode;
import com.dev.conf.global.error.exception.GeneralException;

public class UserNotFoundException extends GeneralException {

    public UserNotFoundException() {
        super(StatusCode.USER_NOT_FOUND);
    }
}
