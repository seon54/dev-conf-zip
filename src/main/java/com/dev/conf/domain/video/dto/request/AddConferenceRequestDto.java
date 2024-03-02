package com.dev.conf.domain.video.dto.request;

import com.dev.conf.domain.video.enums.ConferenceCategory;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddConferenceRequestDto(
        @NotNull
        String url,
        @NotNull
        String title,
        @NotNull
        String conferenceName,
        @NotNull
        String year,
        @NotNull
        ConferenceCategory conferenceCategory,
        List<String> hashtagList
) {
}