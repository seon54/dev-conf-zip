package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.video.entity.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class VideoHashtagBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void upsert(long conferenceId, List<Hashtag> hashtags) {
        String sql = "INSERT INTO video_hashtag(conference_id, hashtag_id) VALUES (?, ?) ON CONFLICT DO NOTHING;";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, conferenceId);
                ps.setLong(2, hashtags.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return hashtags.size();
            }
        });
    }
}
