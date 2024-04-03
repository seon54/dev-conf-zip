package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.video.entity.Hashtag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoHashtagBulkRepositoryTest {

    @InjectMocks
    private VideoHashtagBulkRepository videoHashtagBulkRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Hashtag hashtag1;

    @Mock
    private Hashtag hashtag2;

    @DisplayName("VideoHashtag upsert 성공")
    @Test
    void testUpsert() {
        videoHashtagBulkRepository.upsert(1, List.of(hashtag1, hashtag2));

        verify(jdbcTemplate, times(1)).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

}