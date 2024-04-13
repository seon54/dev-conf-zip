package com.dev.conf.domain.video.dto.response;

import java.util.List;

public record ConferenceListResponseDto(
        List<ConferenceResponseDto> conferenceList,
        int totalPages,
        int size,
        long totalElements,
        int number
) {
}
