package com.dev.conf.domain.video.service.impl;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.domain.video.entity.Hashtag;
import com.dev.conf.domain.video.entity.VideoHashtag;
import com.dev.conf.domain.video.exception.ConferenceExistException;
import com.dev.conf.domain.video.mapper.ConferenceMapper;
import com.dev.conf.domain.video.repository.ConferenceRepository;
import com.dev.conf.domain.video.repository.HashtagBulkRepository;
import com.dev.conf.domain.video.repository.HashtagRepository;
import com.dev.conf.domain.video.repository.VideoHashtagRepository;
import com.dev.conf.domain.video.service.ConferenceService;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void addConference(User user, AddConferenceRequestDto addConferenceRequestDto) {
        checkAlreadyExists(user, addConferenceRequestDto);

        Conference conference = conferenceRepository.save(conferenceMapper.toConference(addConferenceRequestDto, user));
        saveHashtag(addConferenceRequestDto, conference);
    }

    private void checkAlreadyExists(User user, AddConferenceRequestDto addConferenceRequestDto) {
        if (conferenceRepository.existsByUserAndConferenceUrl(user, addConferenceRequestDto.conferenceUrl())) {
            throw new ConferenceExistException();
        }
    }

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

}
