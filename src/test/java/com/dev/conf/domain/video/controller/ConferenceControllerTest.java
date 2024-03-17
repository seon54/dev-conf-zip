package com.dev.conf.domain.video.controller;

import com.dev.conf.docs.RestDocsBaseTest;
import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.domain.video.service.impl.ConferenceServiceImpl;
import com.dev.conf.global.security.filter.JwtAuthenticationFilter;
import com.dev.conf.security.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.List;

import static com.dev.conf.docs.ApiDocumentUtils.getDocumentRequest;
import static com.dev.conf.docs.ApiDocumentUtils.getDocumentResponse;
import static com.dev.conf.global.common.enums.StatusCode.CREATED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockOAuth2User
@WebMvcTest(
        controllers = ConferenceController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtAuthenticationFilter.class})
        }
)
class ConferenceControllerTest extends RestDocsBaseTest {

    @MockBean
    private ConferenceServiceImpl conferenceService;

    @DisplayName("[POST] 컨퍼런스 & 태그 추가 성공")
    @Test
    void testAddConferenceWithTags() throws Exception {
        doNothing().when(conferenceService).addConference(any(User.class), any(AddConferenceRequestDto.class));

        AddConferenceRequestDto dto = getAddConferenceRequestDtoWithTags();
        mockMvc.perform(post("/videos")
                        .content(toJson(dto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(CREATED.getCode()))
                .andExpect(jsonPath("$.message").value(CREATED.getMessage()))
                .andDo(document("video",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("conferenceUrl").type(STRING).description("영상 URL"),
                                fieldWithPath("title").type(STRING).description("컨퍼런스 이름"),
                                fieldWithPath("conferenceName").type(STRING).description("영상 제목"),
                                fieldWithPath("conferenceYear").type(NUMBER).description("컨퍼런스 연도"),
                                fieldWithPath("conferenceCategory").type(STRING).description("영상 분류"),
                                fieldWithPath("hashtagList").type(ARRAY).description("해시태그 목록").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(STRING).description("상태 메세지")
                        )));

    }

    @DisplayName("[POST] 컨퍼런스 추가 성공")
    @Test
    void testAddConference() throws Exception {
        doNothing().when(conferenceService).addConference(any(User.class), any(AddConferenceRequestDto.class));

        AddConferenceRequestDto dto = getAddConferenceRequestDtoWithoutTags();
        mockMvc.perform(post("/videos")
                        .content(toJson(dto))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(CREATED.getCode()))
                .andExpect(jsonPath("$.message").value(CREATED.getMessage()));
    }

    private static AddConferenceRequestDto getAddConferenceRequestDtoWithTags() {
        return new AddConferenceRequestDto(
                "www.dev-conf.com/videos/1",
                "Dev Conf",
                "Backend 1",
                2024,
                ConferenceCategory.BACKEND,
                List.of("tag1", "tag2")
        );
    }

    private static AddConferenceRequestDto getAddConferenceRequestDtoWithoutTags() {
        return new AddConferenceRequestDto(
                "www.dev-conf.com/videos/1",
                "Dev Conf",
                "Backend 1",
                2024,
                ConferenceCategory.BACKEND,
                null
        );
    }

}