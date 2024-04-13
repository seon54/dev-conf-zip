package com.dev.conf.domain.video.controller;

import com.dev.conf.domain.video.dto.ConferenceDetailDto;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateStatusRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateTagRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;
import com.dev.conf.domain.video.service.ConferenceService;
import com.dev.conf.global.common.dto.ApiResponse;
import com.dev.conf.global.common.enums.StatusCode;
import com.dev.conf.global.security.model.AuthUser;
import com.dev.conf.global.security.model.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/videos")
@RequiredArgsConstructor
@RestController
public class ConferenceController {

    private final ConferenceService conferenceService;

    /**
     * 컨퍼런스 저장
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<Object> addConference(@AuthUser CustomOAuth2User oAuth2User,
                                             @Valid @RequestBody AddConferenceRequestDto addConferenceRequestDto) {
        conferenceService.addConference(oAuth2User.getUser(), addConferenceRequestDto);
        return ApiResponse.success(StatusCode.CREATED);
    }

    /**
     * 컨퍼런스 목록 조회
     */
    @GetMapping
    public ApiResponse<Object> getConferenceList(@AuthUser CustomOAuth2User oAuth2User,
                                                 @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(conferenceService.getConferenceList(oAuth2User.getUser(), pageable));
    }

    /**
     * 컨퍼런스 상세 조회
     */
    @GetMapping("/{id}")
    public ApiResponse<Object> getConferenceDetail(@AuthUser CustomOAuth2User oAuth2User,
                                                   @PathVariable long id) {
        return ApiResponse.success(conferenceService.getConferenceDetail(oAuth2User.getUser(), id));
    }

    /**
     * 컨퍼런스 상태 변경
     */
    @PatchMapping("/{id}/status")
    public ApiResponse<Object> updateStatus(@AuthUser CustomOAuth2User oAuth2User,
                                            @PathVariable long id,
                                            @Valid @RequestBody UpdateStatusRequestDto updateStatusRequestDto) {
        ConferenceStatusResponseDto conferenceStatusResponseDto = conferenceService.updateStatus(
                oAuth2User.getUser(),
                id,
                updateStatusRequestDto
        );
        return ApiResponse.success(conferenceStatusResponseDto);
    }

    /**
     * 컨퍼런스 해시태그 수정
     */
    @PatchMapping("/{id}/tags")
    public ApiResponse<Object> updateTags(@AuthUser CustomOAuth2User oAuth2User,
                                          @PathVariable long id,
                                          @Valid @RequestBody UpdateTagRequestDto updateTagRequestDto) {
        ConferenceDetailDto dto = conferenceService.updateTags(oAuth2User.getUser(), id, updateTagRequestDto);
        return ApiResponse.success(dto);
    }
}
