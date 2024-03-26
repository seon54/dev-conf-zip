package com.dev.conf.domain.video.controller;

import com.dev.conf.docs.RestDocsBaseTest;
import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.request.AddConferenceRequestDto;
import com.dev.conf.domain.video.dto.request.UpdateStatusRequestDto;
import com.dev.conf.domain.video.dto.response.ConferenceDetailResponseDto;
import com.dev.conf.domain.video.dto.response.ConferenceStatusResponseDto;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.domain.video.enums.ConferenceStatus;
import com.dev.conf.domain.video.service.impl.ConferenceServiceImpl;
import com.dev.conf.global.security.filter.JwtAuthenticationFilter;
import com.dev.conf.security.WithMockOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import static com.dev.conf.docs.ApiDocumentUtils.getDocumentRequest;
import static com.dev.conf.docs.ApiDocumentUtils.getDocumentResponse;
import static com.dev.conf.global.common.enums.StatusCode.CREATED;
import static com.dev.conf.global.common.enums.StatusCode.OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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

    private final static String CONFERENCE_URL1 = "www.dev-conf.com/videos/1";
    private final static String CONFERENCE_URL2 = "www.dev-conf.com/videos/2";
    private final static String TITLE = "Dev Conf";
    private final static String CONFERENCE_NAME1 = "conference 1";
    private final static String CONFERENCE_NAME2 = "conference 2";

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
                .andDo(document("video-create",
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

    @DisplayName("[GET] 컨퍼런스 목록 조회 성공")
    @Test
    void testGetConferenceList() throws Exception {
        List<ConferenceDetailResponseDto> conferences = getConferenceDetailResponseDtos();
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(DESC, "id"));
        Page<ConferenceDetailResponseDto> page = new PageImpl<>(conferences, pageable, conferences.size());

        when(conferenceService.getConferenceList(any(User.class), any(Pageable.class))).thenReturn(page);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", String.valueOf(pageSize));

        mockMvc.perform(get("/videos").queryParams(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(OK.getCode()))
                .andExpect(jsonPath("$.message").value(OK.getMessage()))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(conferences.size()))
                .andExpect(jsonPath("$.data.size").value(pageSize))
                .andExpect(jsonPath("$.data.content[0].id").value(2L))
                .andExpect(jsonPath("$.data.content[0].title").value(TITLE))
                .andExpect(jsonPath("$.data.content[0].conferenceUrl").value(CONFERENCE_URL2))
                .andExpect(jsonPath("$.data.content[0].conferenceName").value(CONFERENCE_NAME2))
                .andExpect(jsonPath("$.data.content[0].conferenceYear").value(2024))
                .andExpect(jsonPath("$.data.content[0].conferenceCategory").value(ConferenceCategory.BACKEND.name()))
                .andExpect(jsonPath("$.data.content[0].conferenceStatus").value(ConferenceStatus.BEFORE_WATCHING.name()))
                .andExpect(jsonPath("$.data.content[1].id").value(1L))
                .andExpect(jsonPath("$.data.content[1].title").value(TITLE))
                .andExpect(jsonPath("$.data.content[1].conferenceUrl").value(CONFERENCE_URL1))
                .andExpect(jsonPath("$.data.content[1].conferenceName").value(CONFERENCE_NAME1))
                .andExpect(jsonPath("$.data.content[1].conferenceYear").value(2024))
                .andExpect(jsonPath("$.data.content[1].conferenceCategory").value(ConferenceCategory.BACKEND.name()))
                .andExpect(jsonPath("$.data.content[1].conferenceStatus").value(ConferenceStatus.WATCHING.name()))
                .andDo(document("video-list",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        queryParameters(
                                parameterWithName("size").description("요청하는 목록의 페이징 사이즈"),
                                parameterWithName("page").description("요청하는 목록 페이지")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(STRING).description("상태 메세지"),
                                fieldWithPath("data.content[].id").type(NUMBER).description("컨퍼런스 ID"),
                                fieldWithPath("data.content[].title").type(STRING).description("컨퍼런스 제목"),
                                fieldWithPath("data.content[].conferenceUrl").type(STRING).description("컨퍼런스 URL"),
                                fieldWithPath("data.content[].conferenceName").type(STRING).description("컨퍼런스 영상 제목"),
                                fieldWithPath("data.content[].conferenceYear").type(NUMBER).description("컨퍼런스 연도"),
                                fieldWithPath("data.content[].conferenceCategory").type(STRING).description("카테고리"),
                                fieldWithPath("data.content[].conferenceStatus").type(STRING).description("상태값"),
                                fieldWithPath("data.totalPages").type(NUMBER).description("전체 페이지 수"),
                                fieldWithPath("data.totalElements").type(NUMBER).description("전체 데이터 수"),
                                fieldWithPath("data.size").type(NUMBER).description("데이터 개수"),
                                fieldWithPath("data.pageable.pageNumber").type(NUMBER).description(""),
                                fieldWithPath("data.pageable.pageSize").type(NUMBER).description(""),
                                fieldWithPath("data.pageable.sort.empty").type(BOOLEAN).description(""),
                                fieldWithPath("data.pageable.sort.sorted").type(BOOLEAN).description(""),
                                fieldWithPath("data.pageable.sort.unsorted").type(BOOLEAN).description(""),
                                fieldWithPath("data.pageable.offset").type(NUMBER).description(""),
                                fieldWithPath("data.pageable.paged").type(BOOLEAN).description(""),
                                fieldWithPath("data.pageable.unpaged").type(BOOLEAN).description(""),
                                fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("data.first").type(BOOLEAN).description("첫 페이지 여부"),
                                fieldWithPath("data.number").type(NUMBER).description("페이지 번호"),
                                fieldWithPath("data.sort.empty").type(BOOLEAN).description(""),
                                fieldWithPath("data.sort.sorted").type(BOOLEAN).description(""),
                                fieldWithPath("data.sort.unsorted").type(BOOLEAN).description(""),
                                fieldWithPath("data.numberOfElements").type(NUMBER).description(""),
                                fieldWithPath("data.empty").type(BOOLEAN).description("")
                        )));
    }

    @DisplayName("[PATCH] 상태값 변경")
    @Test
    void testUpdateStatus() throws Exception {
        long id = 1;
        ConferenceStatus status = ConferenceStatus.WATCHING;
        ConferenceStatusResponseDto dto = new ConferenceStatusResponseDto(id, status);
        when(conferenceService.updateStatus(any(User.class), anyLong(), any(UpdateStatusRequestDto.class))).thenReturn(dto);

        UpdateStatusRequestDto requestDto = new UpdateStatusRequestDto(status);
        mockMvc.perform(patch("/videos/{id}/status", id)
                        .contentType(APPLICATION_JSON)
                        .content(toJson(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(OK.getCode()))
                .andExpect(jsonPath("$.message").value(OK.getMessage()))
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.conferenceStatus").value(String.valueOf(status)))
                .andDo(document("update-status",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("컨퍼런스 ID")
                        ),
                        requestFields(
                                fieldWithPath("conferenceStatus").type(STRING).description("변경할 상태값")
                        ),
                        responseFields(
                                fieldWithPath("code").type(NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(STRING).description("상태 메세지"),
                                fieldWithPath("data.id").type(NUMBER).description("컨퍼런스 ID"),
                                fieldWithPath("data.conferenceStatus").type(STRING).description("상태값")
                        )));
    }

    private static List<ConferenceDetailResponseDto> getConferenceDetailResponseDtos() {
        return Arrays.asList(
                new ConferenceDetailResponseDto(2L, TITLE, CONFERENCE_URL2, CONFERENCE_NAME2, 2024, ConferenceCategory.BACKEND, ConferenceStatus.BEFORE_WATCHING),
                new ConferenceDetailResponseDto(1L, TITLE, CONFERENCE_URL1, CONFERENCE_NAME1, 2024, ConferenceCategory.BACKEND, ConferenceStatus.WATCHING)
        );
    }

    private static AddConferenceRequestDto getAddConferenceRequestDtoWithTags() {
        return new AddConferenceRequestDto(
                CONFERENCE_URL1,
                TITLE,
                CONFERENCE_NAME1,
                2024,
                ConferenceCategory.BACKEND,
                List.of("tag1", "tag2")
        );
    }

    private static AddConferenceRequestDto getAddConferenceRequestDtoWithoutTags() {
        return new AddConferenceRequestDto(
                CONFERENCE_URL1,
                TITLE,
                CONFERENCE_NAME1,
                2024,
                ConferenceCategory.BACKEND,
                null
        );
    }

}