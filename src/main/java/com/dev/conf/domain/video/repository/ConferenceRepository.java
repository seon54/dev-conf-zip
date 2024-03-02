package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    boolean existsByUserAndUrl(User user, String url);

}
