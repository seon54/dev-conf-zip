package com.dev.conf.domain.video.service;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.domain.video.entity.Hashtag;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.domain.video.exception.ConferenceExistException;
import com.dev.conf.domain.video.mapper.ConferenceMapper;
import com.dev.conf.domain.video.repository.ConferenceRepository;
import com.dev.conf.domain.video.repository.HashtagBulkRepository;
import com.dev.conf.domain.video.repository.HashtagRepository;
import com.dev.conf.domain.video.repository.VideoHashtagRepository;
import com.dev.conf.domain.video.service.impl.ConferenceServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConferenceServiceTest {

    @InjectMocks
    private ConferenceServiceImpl conferenceService;
    @Mock
    private ConferenceRepository conferenceRepository;
    @Mock
    private HashtagRepository hashtagRepository;
    @Mock
    private HashtagBulkRepository hashtagBulkRepository;
    @Mock
    private VideoHashtagRepository videoHashtagRepository;
    @Mock
    private ConferenceMapper conferenceMapper;
    @Mock
    private User user;
    @Mock
    private Conference conference;
    @Mock
    private Hashtag hashtag1;
    @Mock
    private Hashtag hashtag2;
    private static final String VIDEO_URL = "www.dev-conf.com/videos/1";

    @DisplayName("컨퍼런스 추가 성공")
    @Test
    void testAddConferenceSuccess() {
        List<Hashtag> hashtags = List.of(hashtag1, hashtag2);
        when(conferenceRepository.existsByUserAndConferenceUrl(any(User.class), anyString())).thenReturn(false);
        when(conferenceMapper.toConference(any(AddConferenceRequestDto.class), any(User.class))).thenReturn(conference);
        when(conferenceRepository.save(any(Conference.class))).thenReturn(conference);
        doNothing().when(hashtagBulkRepository).upsert(anyList());
        when(hashtagRepository.findAllByKeyword(anyList())).thenReturn(hashtags);

        AddConferenceRequestDto dto = getAddConferenceRequestDtoWithTags();
        conferenceService.addConference(user, dto);

        verify(conferenceRepository, times(1)).existsByUserAndConferenceUrl(user, VIDEO_URL);
        verify(conferenceRepository, times(1)).save(any(Conference.class));
        verify(hashtagBulkRepository, times(1)).upsert(anyList());
        verify(hashtagRepository, times(1)).findAllByKeyword(anyList());
        verify(videoHashtagRepository, times(1)).saveAll(anyList());
    }

    @DisplayName("컨퍼런스 추가 실패")
    @Test
    void testAddConferenceFail() {
        when(conferenceRepository.existsByUserAndConferenceUrl(any(User.class), anyString())).thenReturn(true);

        AddConferenceRequestDto dto = getAddConferenceRequestDtoWithTags();
        assertThrows(ConferenceExistException.class, () -> conferenceService.addConference(user, dto));

        verify(conferenceRepository, times(1)).existsByUserAndConferenceUrl(user, VIDEO_URL);
    }

    private static AddConferenceRequestDto getAddConferenceRequestDtoWithTags() {
        return new AddConferenceRequestDto(
                VIDEO_URL,
                "Dev Conf",
                "Backend 1",
                2024,
                ConferenceCategory.BACKEND,
                List.of("tag1", "tag2")
        );
    }


}