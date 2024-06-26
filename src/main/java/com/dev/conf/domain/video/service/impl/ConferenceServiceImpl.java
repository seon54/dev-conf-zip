package com.dev.conf.domain.video.service.impl;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.ConferenceDetailDto;
import com.dev.conf.domain.video.dto.KeywordDetailDto;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateStatusRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateTagRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceListResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;
import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.domain.video.entity.Hashtag;
import com.dev.conf.domain.video.entity.VideoHashtag;
import com.dev.conf.domain.video.exception.ConferenceExistException;
import com.dev.conf.domain.video.exception.ConferenceNotFoundException;
import com.dev.conf.domain.video.mapper.ConferenceMapper;
import com.dev.conf.domain.video.repository.*;
import com.dev.conf.domain.video.service.ConferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Service
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtagBulkRepository hashtagBulkRepository;
    private final VideoHashtagRepository videoHashtagRepository;
    private final ConferenceMapper conferenceMapper;
    private final VideoHashtagBulkRepository videoHashtagBulkRepository;

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
        Conference conference = getConference(user, id);

        conference.updateStatus(updateStatusRequestDto.conferenceStatus());
        Conference savedConference = conferenceRepository.save(conference);
        return conferenceMapper.toConferenceStatusResponseDto(savedConference);
    }

    /**
     * 컨퍼런스 목록 조회
     */
    public ConferenceListResponseDto getConferenceList(User user, Pageable pageable) {
        Page<ConferenceDetailDto> conferenceDetailList = conferenceRepository.findAllByUser(user, pageable);
        List<Long> conferenceIds = getConferenceIds(conferenceDetailList);
        Map<Long, List<String>> keywordMap = getKeywordMap(conferenceIds);
        List<ConferenceResponseDto> conferenceList = getResponseDtoList(conferenceDetailList.getContent(), keywordMap);
        return new ConferenceListResponseDto(
                conferenceList,
                conferenceDetailList.getTotalPages(),
                conferenceDetailList.getSize(),
                conferenceDetailList.getTotalElements(),
                conferenceDetailList.getPageable().getPageNumber()
        );
    }

    private static List<Long> getConferenceIds(Page<ConferenceDetailDto> conferenceDetailList) {
        return conferenceDetailList.getContent()
                .stream()
                .map(ConferenceDetailDto::id)
                .toList();
    }

    private Map<Long, List<String>> getKeywordMap(List<Long> conferenceIds) {
        return hashtagRepository.findKeywordDetailDtoListByConferenceIds(conferenceIds)
                .stream()
                .collect(
                        groupingBy(
                                KeywordDetailDto::conferenceId,
                                mapping(KeywordDetailDto::keyword, toList()
                                )
                        )
                );
    }

    private List<ConferenceResponseDto> getResponseDtoList(List<ConferenceDetailDto> conferenceDetailList, Map<Long, List<String>> keywordMap) {
        List<ConferenceResponseDto> conferenceList = new ArrayList<>();
        for (ConferenceDetailDto dto : conferenceDetailList) {
            conferenceList.add(conferenceMapper.toConferenceResponseDto(dto, keywordMap.get(dto.id())));
        }
        return conferenceList;
    }

    /**
     * 컨퍼런스 상세 정보 조회
     */
    public ConferenceResponseDto getConferenceDetail(User user, long id) {
        Conference conference = getConference(user, id);
        List<String> hashtaghList = hashtagRepository.findKeywordsByConference(conference);
        return conferenceMapper.toConferenceResponseDto(conference, hashtaghList);
    }

    /**
     * 컨퍼런스 태그 변경
     */
    @Transactional
    public ConferenceDetailDto updateTags(User user, long id, UpdateTagRequestDto updateTagRequestDto) {
        Conference conference = getConference(user, id);
        upsertHashtags(updateTagRequestDto, conference);
        return conferenceMapper.toConferenceDetailResponseDto(conference);
    }

    private void upsertHashtags(UpdateTagRequestDto updateTagRequestDto, Conference conference) {
        hashtagBulkRepository.upsert(updateTagRequestDto.hashtagList());
        List<Hashtag> hashtags = hashtagRepository.findAllByKeyword(updateTagRequestDto.hashtagList());
        videoHashtagBulkRepository.upsert(conference.getId(), hashtags);
    }

    private Conference getConference(User user, long id) {
        return conferenceRepository.findByIdAndUser(id, user).orElseThrow(ConferenceNotFoundException::new);
    }

}
