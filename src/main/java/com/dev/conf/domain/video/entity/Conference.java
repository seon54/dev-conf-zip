package com.dev.conf.domain.video.entity;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.global.common.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Conference extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "conference")
    private List<VideoHashtag> hashtags = new ArrayList<>();

    @Comment("컨퍼런스 제목")
    @Column
    private String title;

    @Comment("컨퍼런스 URL")
    @Column
    private String url;

    @Comment("영상 제목")
    @Column
    private String conferenceName;

    @Comment("컨퍼런스 발표 연도")
    @Column
    private String year;

    @Comment("컨퍼런스 분류")
    @Column
    @Enumerated(EnumType.STRING)
    private ConferenceCategory conferenceCategory;

    @Builder
    public Conference(String url, String title, String conferenceName, String year, ConferenceCategory conferenceCategory, User user) {
        this.user = user;
        this.title = title;
        this.url = url;
        this.conferenceName = conferenceName;
        this.year = year;
        this.conferenceCategory = conferenceCategory;
    }
}
