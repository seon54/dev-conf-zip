package com.dev.conf.domain.video.service;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateStatusRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateTagRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceDetailResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ConferenceService {

    void addConference(User user, AddConferenceRequestDto addConferenceRequestDto);

    ConferenceStatusResponseDto updateStatus(User user, long id, UpdateStatusRequestDto updateStatusRequestDto);

    Page<ConferenceDetailResponseDto> getConferenceList(User user, Pageable pageable);

    ConferenceResponseDto getConferenceDetail(User user, long id);

    ConferenceDetailResponseDto updateTags(User user, long id, UpdateTagRequestDto updateTagRequestDto);
}
