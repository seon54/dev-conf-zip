package com.dev.conf.domain.video.controller;

import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateStatusRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;
import com.dev.conf.domain.video.service.ConferenceService;
import com.dev.conf.global.common.dto.ApiResponse;
import com.dev.conf.global.security.model.AuthUser;
import com.dev.conf.global.security.model.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/videos")
@RequiredArgsConstructor
@RestController
public class ConferenceController {

    private final ConferenceService conferenceService;

    @PostMapping
    public ApiResponse<Object> addConference(@AuthUser CustomOAuth2User oAuth2User,
                                             @Valid @RequestBody AddConferenceRequestDto addConferenceRequestDto) {
        conferenceService.addConference(oAuth2User.getUser(), addConferenceRequestDto);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Object> updateStatus(@AuthUser CustomOAuth2User oAuth2User,
                                            @PathVariable long id,
                                            @Valid @RequestBody UpdateStatusRequestDto updateStatusRequestDto) {
        ConferenceStatusResponseDto conferenceStatusResponseDto = conferenceService.updateStatus(
                oAuth2User.getUser(),
                id,
                updateStatusRequestDto
        );
        return ApiResponse.success(conferenceStatusResponseDto);
    }
}
