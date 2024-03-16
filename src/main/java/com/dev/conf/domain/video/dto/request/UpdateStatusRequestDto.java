package com.dev.conf.domain.video.dto.request;

import com.dev.conf.domain.video.enums.ConferenceStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequestDto(
        @NotNull
        ConferenceStatus conferenceStatus
) {
}
