package com.dev.conf.domain.user.entity;

import com.dev.conf.domain.video.entity.Conference;
import com.dev.conf.global.common.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "users")
@Entity
public class User extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String provider;

    @Column
    private String providerId;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Conference> conferenceList = new ArrayList<>();

    public User(String username, String email, String provider, String providerId) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }
}
