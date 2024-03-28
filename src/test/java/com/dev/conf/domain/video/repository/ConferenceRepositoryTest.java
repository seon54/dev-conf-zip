package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.user.repository.UserRepository;
import com.dev.conf.domain.video.dto.response.ConferenceDetailResponseDto;
import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ConferenceRepositoryTest {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String VIDEO_URL = "www.conf.com/video/1";

    @DisplayName("사용자 & URL로 컨퍼런스 조회")
    @Test
    void testExistsByUserAndConferenceUrl() {
        // given
        User user = getUser();
        userRepository.save(user);
        Conference conference = new Conference(VIDEO_URL, "title", "conference 1", 2024, ConferenceCategory.BACKEND, user);
        conferenceRepository.save(conference);

        // when
        boolean result = conferenceRepository.existsByUserAndConferenceUrl(user, VIDEO_URL);
        boolean result2 = conferenceRepository.existsByUserAndConferenceUrl(user, "www.conf.com/video/2");

        // then
        assertThat(result).isTrue();
        assertThat(result2).isFalse();
    }

    @DisplayName("사용자 & ID로 컨퍼런스 조회")
    @Test
    void testFindByIdAndUser() {
        // given
        User user = getUser();
        userRepository.save(user);
        Conference conference = new Conference(VIDEO_URL, "title", "conference 1", 2024, ConferenceCategory.BACKEND, user);
        conferenceRepository.save(conference);

        // when
        Optional<Conference> result = conferenceRepository.findByIdAndUser(conference.getId(), user);
        Optional<Conference> result2 = conferenceRepository.findByIdAndUser(2L, user);

        // then
        assertThat(result).isPresent().contains(conference);
        assertThat(result2).isNotPresent();
    }

    @DisplayName("사용자 & Page 컨퍼런스 목록 조회")
    @Test
    void testFindAllByUser() {
        // given
        User user = getUser();
        userRepository.save(user);
        Conference conference = new Conference(VIDEO_URL, "title", "conference 1", 2024, ConferenceCategory.BACKEND, user);
        conferenceRepository.save(conference);

        // when
        Page<ConferenceDetailResponseDto> result = conferenceRepository.findAllByUser(user, PageRequest.of(0, 5));

        // then
        assertThat(result.getContent().get(0).id()).isEqualTo(conference.getId());
        assertThat(result.getContent().get(0).title()).isEqualTo(conference.getTitle());
        assertThat(result.getContent().get(0).conferenceUrl()).isEqualTo(conference.getConferenceUrl());
        assertThat(result.getContent().get(0).conferenceName()).isEqualTo(conference.getConferenceName());
        assertThat(result.getContent().get(0).conferenceYear()).isEqualTo(conference.getConferenceYear());
        assertThat(result.getContent().get(0).conferenceStatus()).isEqualTo(conference.getConferenceStatus());
        assertThat(result.getContent().get(0).conferenceCategory()).isEqualTo(conference.getConferenceCategory());
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    private static User getUser() {
        return new User("user", "test@test.com", "provider", "providerId");
    }

}