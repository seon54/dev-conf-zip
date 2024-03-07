package com.dev.conf.domain.video.repository;

import lombok.NonNull;
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
public class HashtagBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void upsert(List<String> keywords) {
        String sql = "INSERT INTO hashtag(keyword) VALUES (?) ON CONFLICT DO NOTHING ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, keywords.get(i));
            }

            @Override
            public int getBatchSize() {
                return keywords.size();
            }
        });
    }
}
