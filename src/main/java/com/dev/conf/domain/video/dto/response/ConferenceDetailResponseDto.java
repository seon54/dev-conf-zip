package com.dev.conf.domain.video.dto.response;

import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.domain.video.enums.ConferenceStatus;

public record ConferenceDetailResponseDto(
        long id,
        String title,
        String conferenceUrl,
        String conferenceName,
        int conferenceYear,
        ConferenceCategory conferenceCategory,
        ConferenceStatus conferenceStatus
) {
}
