package com.dev.conf.domain.video.dto.response;

import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.domain.video.enums.ConferenceStatus;

import java.util.List;

public record ConferenceResponseDto(
        long id,
        String title,
        String conferenceUrl,
        String conferenceName,
        int conferenceYear,
        ConferenceCategory conferenceCategory,
        ConferenceStatus conferenceStatus,
        List<String> hashtagList
) {
}
