package com.dev.conf.domain.video.exception;

import com.dev.conf.global.common.enums.StatusCode;
import com.dev.conf.global.error.exception.GeneralException;

public class ConferenceExistException extends GeneralException {

    public ConferenceExistException() {
        super(StatusCode.CONFERENCE_ALREADY_EXISTS);
    }
}
