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

@Table(name = "conference")
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
    private final List<VideoHashtag> hashtags = new ArrayList<>();

    @Comment("컨퍼런스 제목")
    @Column
    private String title;

    @Comment("컨퍼런스 URL")
    @Column
    private String conferenceUrl;

    @Comment("영상 제목")
    @Column
    private String conferenceName;

    @Comment("컨퍼런스 발표 연도")
    @Column
    private int conferenceYear;

    @Comment("컨퍼런스 분류")
    @Column
    @Enumerated(EnumType.STRING)
    private ConferenceCategory conferenceCategory;

    @Builder
    public Conference(String conferenceUrl, String title, String conferenceName, int conferenceYear, ConferenceCategory conferenceCategory, User user) {
        this.user = user;
        this.title = title;
        this.conferenceUrl = conferenceUrl;
        this.conferenceName = conferenceName;
        this.conferenceYear = conferenceYear;
        this.conferenceCategory = conferenceCategory;
    }
}
