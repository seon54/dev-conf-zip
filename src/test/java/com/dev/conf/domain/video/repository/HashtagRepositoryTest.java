package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.user.repository.UserRepository;
import com.dev.conf.domain.video.dto.KeywordDetailDto;
import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.domain.video.entity.Hashtag;
import com.dev.conf.domain.video.entity.VideoHashtag;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HashtagRepositoryTest {

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String TAG = "tag1";
    @Autowired
    private VideoHashtagRepository videoHashtagRepository;

    @DisplayName("키워드 목록으로 해시태그 조회")
    @Test
    void testFindAllByKeyword() {
        // given
        Hashtag hashtag = new Hashtag(TAG);
        hashtagRepository.save(hashtag);

        // when
        List<Hashtag> allByKeyword = hashtagRepository.findAllByKeyword(List.of(TAG));

        // then
        assertThat(allByKeyword).hasSize(1).contains(hashtag);
    }

    @DisplayName("컨퍼런스로 해시태그 조회")
    @Test
    void testFindKeywordsByConference() {
        // given
        User user = getUser();
        userRepository.save(user);
        Conference conference = getConference(user);
        conferenceRepository.save(conference);
        Hashtag hashtag = new Hashtag(TAG);
        hashtagRepository.save(hashtag);
        videoHashtagRepository.save(new VideoHashtag(conference, hashtag));

        // when
        List<String> keywords = hashtagRepository.findKeywordsByConference(conference);

        // then
        assertThat(keywords).hasSize(1).contains(TAG);
    }

    @DisplayName("컨퍼런스 ID 목록으로 해시태그 조회")
    @Test
    void testFindKeywordDetailDtoListByConferenceIds() {
        // given
        User user = getUser();
        userRepository.save(user);
        Conference conference = getConference(user);
        conferenceRepository.save(conference);
        Hashtag hashtag = new Hashtag(TAG);
        hashtagRepository.save(hashtag);
        videoHashtagRepository.save(new VideoHashtag(conference, hashtag));

        // when
        List<KeywordDetailDto> keywordDetailDtos = hashtagRepository.findKeywordDetailDtoListByConferenceIds(List.of(conference.getId()));

        // then
        assertThat(keywordDetailDtos).hasSize(1);
        assertThat(keywordDetailDtos.get(0).conferenceId()).isEqualTo(conference.getId());
        assertThat(keywordDetailDtos.get(0).keyword()).isEqualTo(TAG);
    }


    private static User getUser() {
        return new User("user", "test@test.com", "provider", "providerId");
    }

    private static Conference getConference(User user) {
        Conference conference = new Conference("www.conf.com/video/1", "title", "conference 1", 2024, ConferenceCategory.BACKEND, user);
        return conference;
    }

}