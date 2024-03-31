package com.dev.conf.domain.video.mapper;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceDetailResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;
import com.dev.conf.domain.video.entity.Conference;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedSourcePolicy = IGNORE)
public interface ConferenceMapper {

    Conference toConference(AddConferenceRequestDto addConferenceRequestDto, User user);

    ConferenceStatusResponseDto toConferenceStatusResponseDto(Conference conference);

    ConferenceDetailResponseDto toConferenceDetailResponseDto(Conference conference);
}
