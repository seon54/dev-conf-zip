package com.dev.conf.domain.video.entity;

import com.dev.conf.domain.user.entity.User;
import com.dev.conf.domain.video.enums.ConferenceCategory;
import com.dev.conf.domain.video.enums.VideoTime;
import com.dev.conf.global.common.entity.TimeBaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @Column
    private String title;

    @Column
    private String conferenceName;

    @Column
    private String year;

    @Column
    @Enumerated(EnumType.STRING)
    private ConferenceCategory conferenceCategory;

    @Column
    @Enumerated(EnumType.STRING)
    private VideoTime videoTime;
}
