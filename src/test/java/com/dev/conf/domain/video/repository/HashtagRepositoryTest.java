package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.video.entity.Hashtag;
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

    private static final String TAG = "tag1";

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

}