create table if not exists MEMBER (
    id bigint auto_increment not null,
    name varchar(255) not null unique
);

create table if not exists CONFERENCE_TIME (
    id bigint auto_increment not null,
    time int(24) not null unique
);

insert into CONFERENCE_TIME(time) values (6);
insert into CONFERENCE_TIME(time) values (10);