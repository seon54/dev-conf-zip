package com.dev.conf.domain.video.repository;

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
class HashtagBulkRepositoryTest {

    @InjectMocks
    private HashtagBulkRepository hashtagBulkRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @DisplayName("해시태그 upsert 성공")
    @Test
    void testUpsert() {
        hashtagBulkRepository.upsert(List.of("tag1", "tag2", "tag3"));

        verify(jdbcTemplate, times(1)).batchUpdate(anyString(), any(BatchPreparedStatementSetter.class));
    }

}