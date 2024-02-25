package com.dev.conf.domain.video.enums;

public enum VideoTime {
    UNDER_10MIN("10분 이하"),
    UNDER_20MIN("20분 이하"),
    UNDER_30MIN("30분 이하"),
    UNDER_40MIN("40분 이하"),
    UNDER_50MIN("50분 이하"),
    UNDER_1HOUR("1시간 이하"),
    MORE_THAN_1HOUR("1시간 초과");

    private String time;

    VideoTime(String time) {
        this.time = time;
    }
}
