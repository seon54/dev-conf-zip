package com.dev.conf.domain.video.service;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.ConferenceDetailDto;
import com.dev.conf.domain.video.dto.KeywordDetailDto;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateStatusRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateTagRequestDto;
import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.domain.video.entity.Hashtag;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.domain.video.enums.ConferenceStatus;
import com.dev.conf.domain.video.exception.ConferenceExistException;
import com.dev.conf.domain.video.exception.ConferenceNotFoundException;
import com.dev.conf.domain.video.mapper.ConferenceMapper;
import com.dev.conf.domain.video.repository.*;
import com.dev.conf.domain.video.service.impl.ConferenceServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
    private VideoHashtagBulkRepository videoHashtagBulkRepository;
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

    @DisplayName("컨퍼런스 상태 변경 성공")
    @Test
    void testUpdateStatus() {
        when(conferenceRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(conference));
        when(conferenceRepository.save(any(Conference.class))).thenReturn(conference);

        UpdateStatusRequestDto dto = new UpdateStatusRequestDto(ConferenceStatus.WATCHING);
        conferenceService.updateStatus(user, conference.getId(), dto);

        verify(conferenceRepository, times(1)).findByIdAndUser(conference.getId(), user);
        verify(conferenceRepository, times(1)).save(conference);
    }

    @DisplayName("컨퍼런스 상태 변경 실패")
    @Test
    void testUpdateStatusFail() {
        when(conferenceRepository.findByIdAndUser(anyLong(), any(User.class))).thenThrow(ConferenceNotFoundException.class);

        UpdateStatusRequestDto dto = new UpdateStatusRequestDto(ConferenceStatus.WATCHING);
        assertThrows(ConferenceNotFoundException.class, () -> conferenceService.updateStatus(user, 2L, dto));
    }

    @DisplayName("컨퍼런스 목록 조회 성공")
    @Test
    void testGetConferenceList() {
        ConferenceDetailDto dto = getConferenceDetailResponseDto();
        Pageable pageable = PageRequest.of(0, 5);
        Page page = new PageImpl(List.of(dto), pageable, 1);
        when(conferenceRepository.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(page);

        List<KeywordDetailDto> keywordDetailDtoList = List.of(new KeywordDetailDto(1, "tag1"));
        when(hashtagRepository.findKeywordDetailDtoListByConferenceIds(anyList())).thenReturn(keywordDetailDtoList);

        conferenceService.getConferenceList(user, pageable);

        verify(conferenceRepository, times(1)).findAllByUser(user, pageable);
        verify(hashtagRepository, times(1)).findKeywordDetailDtoListByConferenceIds(List.of(1L));
    }

    @DisplayName("컨퍼런스 상세 조회 성공")
    @Test
    void testGetConferenceDetail() {
        when(conferenceRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(conference));
        when(hashtagRepository.findKeywordsByConference(any(Conference.class))).thenReturn(List.of("tag1", "tag2"));

        conferenceService.getConferenceDetail(user, 1);

        verify(conferenceRepository, times(1)).findByIdAndUser(1, user);
        verify(hashtagRepository, times(1)).findKeywordsByConference(conference);
    }

    @DisplayName("컨퍼런스 상세 조회 실패")
    @Test
    void testGetConferenceDetailFail() {
        when(conferenceRepository.findByIdAndUser(anyLong(), any(User.class))).thenThrow(ConferenceNotFoundException.class);

        assertThrows(ConferenceNotFoundException.class, () -> conferenceService.getConferenceDetail(user, 1));
    }

    @DisplayName("컨퍼런스 태그 변경 성공")
    @Test
    void testUpdateTags() {
        when(conferenceRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(conference));
        doNothing().when(hashtagBulkRepository).upsert(anyList());
        when(hashtagRepository.findAllByKeyword(anyList())).thenReturn(List.of(hashtag1, hashtag2));
        when(conferenceMapper.toConferenceDetailResponseDto(any(Conference.class))).thenReturn(getConferenceDetailResponseDto());

        UpdateTagRequestDto requestDto = new UpdateTagRequestDto(List.of("tag1", "tag2"));
        conferenceService.updateTags(user, 1, requestDto);

        verify(conferenceRepository, times(1)).findByIdAndUser(1, user);
        verify(hashtagBulkRepository, times(1)).upsert(requestDto.hashtagList());
        verify(hashtagRepository, times(1)).findAllByKeyword(requestDto.hashtagList());
        verify(conferenceMapper, times(1)).toConferenceDetailResponseDto(conference);
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

    private static ConferenceDetailDto getConferenceDetailResponseDto() {
        return new ConferenceDetailDto(1, "title1", VIDEO_URL, "conference1", 2024, ConferenceCategory.BACKEND, ConferenceStatus.WATCHING);
    }


}