package com.dev.conf.domain.video.dto;

import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.domain.video.enums.ConferenceStatus;

public record ConferenceDetailDto(
        long id,
        String title,
        String conferenceUrl,
        String conferenceName,
        int conferenceYear,
        ConferenceCategory conferenceCategory,
        ConferenceStatus conferenceStatus
) {
}
