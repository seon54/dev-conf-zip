package com.dev.conf.domain.video.service.impl;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateStatusRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceDetailResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;
import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.domain.video.entity.Hashtag;
import com.dev.conf.domain.video.entity.VideoHashtag;
import com.dev.conf.domain.video.exception.ConferenceExistException;
import com.dev.conf.domain.video.exception.ConferenceNotFoundException;
import com.dev.conf.domain.video.mapper.ConferenceMapper;
import com.dev.conf.domain.video.repository.ConferenceRepository;
import com.dev.conf.domain.video.repository.HashtagBulkRepository;
import com.dev.conf.domain.video.repository.HashtagRepository;
import com.dev.conf.domain.video.repository.VideoHashtagRepository;
import com.dev.conf.domain.video.service.ConferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtagBulkRepository hashtagBulkRepository;
    private final VideoHashtagRepository videoHashtagRepository;
    private final ConferenceMapper conferenceMapper;

    /**
     * 컨퍼런스 추가
     */
    @Transactional
    public void addConference(User user, AddConferenceRequestDto addConferenceRequestDto) {
        checkAlreadyExists(user, addConferenceRequestDto);

        Conference conference = conferenceRepository.save(conferenceMapper.toConference(addConferenceRequestDto, user));
        saveHashtag(addConferenceRequestDto, conference);
    }

    /**
     * 컨퍼런스 존재 여부 확인
     */
    private void checkAlreadyExists(User user, AddConferenceRequestDto addConferenceRequestDto) {
        if (conferenceRepository.existsByUserAndConferenceUrl(user, addConferenceRequestDto.conferenceUrl())) {
            throw new ConferenceExistException();
        }
    }

    /**
     * 해시태그 저장
     */
    private void saveHashtag(AddConferenceRequestDto addConferenceRequestDto, Conference conference) {
        if (addConferenceRequestDto.hashtagList() == null || addConferenceRequestDto.hashtagList().isEmpty()) return;

        // 해시태그 upsert
        List<String> keywords = addConferenceRequestDto.hashtagList();
        hashtagBulkRepository.upsert(keywords);

        List<Hashtag> hashtags = hashtagRepository.findAllByKeyword(keywords);

        // VideoHashtag 저장
        List<VideoHashtag> videoHashtags = new ArrayList<>();
        for (Hashtag hashtag : hashtags) {
            videoHashtags.add(new VideoHashtag(conference, hashtag));
        }
        videoHashtagRepository.saveAll(videoHashtags);
    }

    /**
     * 컨퍼런스 상태값 변경
     */
    @Transactional
    public ConferenceStatusResponseDto updateStatus(User user, long id, UpdateStatusRequestDto updateStatusRequestDto) {
        Conference conference = conferenceRepository.findByIdAndUser(id, user)
                .orElseThrow(ConferenceNotFoundException::new);

        conference.updateStatus(updateStatusRequestDto.conferenceStatus());
        Conference savedConference = conferenceRepository.save(conference);
        return conferenceMapper.toConferenceStatusResponseDto(savedConference);
    }

    /**
     * 컨퍼런스 목록 조회
     */
    public Page<ConferenceDetailResponseDto> getConferenceList(User user, Pageable pageable) {
        return conferenceRepository.findAllByUser(user, pageable);
    }

}
