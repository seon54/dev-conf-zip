package com.dev.conf.domain.video.service;

import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.global.security.model.CustomOAuth2User;

public interface ConferenceService {

    void addConference(CustomOAuth2User user, AddConferenceRequestDto addConferenceRequestDto);
}
