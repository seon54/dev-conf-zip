package com.dev.conf.domain.video.repository;

import com.dev.conf.domain.video.dto.KeywordDetailDto;
import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.domain.video.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    @Query("select h from Hashtag  h where h.keyword in :keywords")
    List<Hashtag> findAllByKeyword(@Param("keywords") List<String> keywords);

    @Query("select h.keyword from Hashtag h join fetch VideoHashtag v on v.hashtag = h where v.conference = :conference")
    List<String> findKeywordsByConference(@Param("conference") Conference conference);

    @Query("select new com.dev.conf.domain.video.dto.KeywordDetailDto(v.conference.id, h.keyword) from Hashtag h join fetch VideoHashtag  v on v.hashtag = h where v.conference.id in :conferenceIds order by v.conference.id")
    List<KeywordDetailDto> findKeywordDetailDtoListByConferenceIds(@Param("conferenceIds") List<Long> conferenceIds);

}
