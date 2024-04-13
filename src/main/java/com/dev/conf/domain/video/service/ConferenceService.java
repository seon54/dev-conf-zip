package com.dev.conf.domain.video.service;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.ConferenceDetailDto;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateStatusRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateTagRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceListResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;
import org.springframework.data.domain.Pageable;


public interface ConferenceService {

    void addConference(User user, AddConferenceRequestDto addConferenceRequestDto);

    ConferenceStatusResponseDto updateStatus(User user, long id, UpdateStatusRequestDto updateStatusRequestDto);

    ConferenceListResponseDto getConferenceList(User user, Pageable pageable);

    ConferenceResponseDto getConferenceDetail(User user, long id);

    ConferenceDetailDto updateTags(User user, long id, UpdateTagRequestDto updateTagRequestDto);
}
