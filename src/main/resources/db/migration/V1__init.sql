CREATE TABLE users
(
    id          bigserial primary key not null,
    username    varchar(10)           not null,
    provider    varchar(50),
    provider_id varchar(100),
    email       varchar(100)           not null unique,
    created_at  timestamp(6) default current_timestamp(6),
    updated_at  timestamp(6) default current_timestamp(6)
);

CREATE TABLE conference
(
    id                  bigserial primary key   not null,
    user_id             bigint references users not null,
    title               varchar(30)             not null,
    conference_url      varchar(100)            not null,
    conference_name     varchar(50)             not null,
    conference_category varchar(10)             not null,
    conference_year     int,
    created_at          timestamp(6) default current_timestamp(6),
    updated_at          timestamp(6) default current_timestamp(6)
);

CREATE TABLE hashtag
(
    id      bigserial primary key not null,
    keyword varchar(50)           not null unique
);

CREATE TABLE video_hashtag
(
    id            bigserial primary key        not null,
    conference_id bigint references conference not null,
    hashtag_id    bigint references hashtag    not null
);
