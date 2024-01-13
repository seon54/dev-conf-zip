package com.dev.conf.domain.video.enums;

public enum ConferenceCategory {
    BACKEND("backend"),
    DATA("data"),
    INFRA("infra"),
    WEB("web");

    private String category;

    ConferenceCategory(String category) {
        this.category = category;
    }
}
