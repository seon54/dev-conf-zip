package com.dev.conf.domain.video.mapper;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.ConferenceDetailDto;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;
import com.dev.conf.domain.video.entity.Conference;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedSourcePolicy = IGNORE)
public interface ConferenceMapper {

    Conference toConference(AddConferenceRequestDto addConferenceRequestDto, User user);

    ConferenceStatusResponseDto toConferenceStatusResponseDto(Conference conference);

    ConferenceDetailDto toConferenceDetailResponseDto(Conference conference);

    ConferenceResponseDto toConferenceResponseDto(Conference conference, List<String> hashtagList);

    ConferenceResponseDto toConferenceResponseDto(ConferenceDetailDto conferenceDetailDto, List<String> hashtagList);
}
