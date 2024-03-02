package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.video.entity.VideoHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoHashtagRepository extends JpaRepository<VideoHashtag, Long> {
}
