package com.dev.conf.domain.video.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateTagRequestDto(
        @NotNull
        List<String> hashtagList
) {
}
