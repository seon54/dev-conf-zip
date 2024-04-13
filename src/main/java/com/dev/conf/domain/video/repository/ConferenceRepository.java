package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.dto.ConferenceDetailDto;
import com.dev.conf.domain.video.entity.Conference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    boolean existsByUserAndConferenceUrl(User user, String conferenceUrl);

    Optional<Conference> findByIdAndUser(long id, User user);

    @Query("select new com.dev.conf.domain.video.dto.ConferenceDetailDto" +
            "(c.id, c.title, c.conferenceUrl, c.conferenceName, c.conferenceYear, c.conferenceCategory, c.conferenceStatus) from Conference c where c.user = :user")
    Page<ConferenceDetailDto> findAllByUser(User user, Pageable pageable);

}
