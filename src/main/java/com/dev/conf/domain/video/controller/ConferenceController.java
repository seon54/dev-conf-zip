package com.dev.conf.domain.video.controller;

import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.service.ConferenceService;
import com.dev.conf.global.common.dto.ApiResponse;
import com.dev.conf.global.security.model.AuthUser;
import com.dev.conf.global.security.model.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/videos")
@RequiredArgsConstructor
@RestController
public class ConferenceController {

    private final ConferenceService conferenceService;

    @PostMapping
    public ApiResponse<Object> addConference(@AuthUser CustomOAuth2User user,
                                             @Valid @RequestBody AddConferenceRequestDto addConferenceRequestDto) {
        conferenceService.addConference(user, addConferenceRequestDto);
        return ApiResponse.success();
    }
}
