package com.dev.conf.domain.video.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum ConferenceCategory {
    BACKEND("backend"),
    DATA("data"),
    INFRA("infra"),
    WEB("web"),
    ETC("etc");

    private final String category;

    ConferenceCategory(String category) {
        this.category = category;
    }

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
