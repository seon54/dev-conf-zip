package com.dev.conf.domain.video.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum ConferenceCategory {
    BACKEND,
    DATA,
    INFRA,
    WEB,
    ETC;

    @JsonCreator
    public static ConferenceCategory getConferenceCategory(String value) {
        for (ConferenceCategory conferenceCategory : ConferenceCategory.values()) {
            if (conferenceCategory.name().equals(value)) {
                return conferenceCategory;
            }
        }
        return null;
    }
}
