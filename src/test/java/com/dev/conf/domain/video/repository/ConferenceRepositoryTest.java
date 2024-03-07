package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.user.repository.UserRepository;
import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    private static User getUser() {
        return new User("user", "test@test.com", "provider", "providerId");
    }

}