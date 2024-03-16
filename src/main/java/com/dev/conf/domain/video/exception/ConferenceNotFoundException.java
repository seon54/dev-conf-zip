package com.dev.conf.domain.video.exception;

import com.dev.conf.global.common.enums.StatusCode;
import com.dev.conf.global.error.exception.GeneralException;

public class ConferenceNotFoundException extends GeneralException {

    public ConferenceNotFoundException() {
        super(StatusCode.CONFERENCE_NOT_FOUND);
    }
}
