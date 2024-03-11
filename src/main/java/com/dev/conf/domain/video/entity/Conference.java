package com.dev.conf.domain.video.entity;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.domain.video.enums.ConferenceStatus;
import com.dev.conf.global.common.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "conference")
@DynamicUpdate
@DynamicInsert
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
    @Column(length = 30, nullable = false)
    private String title;

    @Comment("컨퍼런스 URL")
    @Column(length = 100, nullable = false, name = "conference_url")
    private String conferenceUrl;

    @Comment("영상 제목")
    @Column(length = 50, nullable = false, name = "conference_name")
    private String conferenceName;

    @Comment("컨퍼런스 발표 연도")
    @Column(name = "conference_year")
    private int conferenceYear;

    @Comment("컨퍼런스 분류")
    @Column(length = 10, name = "conference_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConferenceCategory conferenceCategory;

    @Comment("컨퍼런스 상태")
    @ColumnDefault("'BEFORE_WATCHING'")
    @Column(length = 15, name = "conference_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConferenceStatus conferenceStatus;

    @Builder
    public Conference(String conferenceUrl, String title, String conferenceName, int conferenceYear, ConferenceCategory conferenceCategory, User user) {
        this.user = user;
        this.title = title;
        this.conferenceUrl = conferenceUrl;
        this.conferenceName = conferenceName;
        this.conferenceYear = conferenceYear;
        this.conferenceCategory = conferenceCategory;
    }

    public void updateStatus(ConferenceStatus conferenceStatus) {
        this.conferenceStatus = conferenceStatus;
    }
}
