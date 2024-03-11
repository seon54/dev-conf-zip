package com.dev.conf.domain.video.service;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateStatusRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;

public interface ConferenceService {

    void addConference(User user, AddConferenceRequestDto addConferenceRequestDto);

    ConferenceStatusResponseDto updateStatus(User user, long id, UpdateStatusRequestDto updateStatusRequestDto);
}
