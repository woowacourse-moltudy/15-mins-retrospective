create table if not exists MEMBER (
    id bigint auto_increment not null,
    name varchar(255) not null unique,

    primary key(id)
);

create table if not exists CONFERENCE_TIME (
    id bigint auto_increment not null,
    time time not null unique,

    primary key(id)
);

create table if not exists ATTENDANCE (
    id bigint auto_increment not null,
    date timestamp,
    member_id bigint not null,
    time_id bigint not null,

    primary key(id),

    foreign key(member_id) references MEMBER(id),
    foreign key(time_id) references CONFERENCE_TIME(id)
);

insert into CONFERENCE_TIME(time) values (PARSEDATETIME('18:00:00', 'HH:mm:ss'));
insert into CONFERENCE_TIME(time) values (PARSEDATETIME('22:00:00', 'HH:mm:ss'));
