package com.dev.conf.domain.video.dto.response;

import com.dev.conf.domain.video.enums.ConferenceStatus;

public record ConferenceStatusResponseDto(
        Long id,
        ConferenceStatus conferenceStatus
) {
}
