package com.dev.conf.domain.video.enums;

public enum ConferenceStatus {
    BEFORE_WATCHING("before"),
    WATCHING("watching"),
    DONE("done");

    private final String status;

    ConferenceStatus(String status) {
        this.status = status;
    }
}
