package com.dev.conf.domain.video.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ConferenceCategory {
    BACKEND("backend"),
    DATA("data"),
    INFRA("infra"),
    WEB("web"),
    ETC("etc");

    private String category;

    ConferenceCategory(String category) {
        this.category = category;
    }

    @JsonCreator
    public static ConferenceCategory getConferenceCategory(String value) {
        for (ConferenceCategory conferenceCategory : ConferenceCategory.values()) {
            if (conferenceCategory.category.equals(value)) {
                return conferenceCategory;
            }
        }
        return null;
    }
}
