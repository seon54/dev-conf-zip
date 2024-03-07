package com.dev.conf.domain.video.service;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;

public interface ConferenceService {

    void addConference(User user, AddConferenceRequestDto addConferenceRequestDto);
}
