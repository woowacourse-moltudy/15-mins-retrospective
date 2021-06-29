create table if not exists MEMBER (
    id bigint auto_increment not null,
    name varchar(255) not null unique,

    primary key(id)
);

create table if not exists CONFERENCE_TIME (
    id bigint auto_increment not null,
    conference_time time not null unique,

    primary key(id)
);

create table if not exists ATTENDANCE (
    id bigint auto_increment not null,
    date date,
    member_id bigint not null,
    conference_time_id bigint not null,

    primary key(id),

    foreign key(member_id) references MEMBER(id),
    foreign key(conference_time_id) references CONFERENCE_TIME(id)
);

insert into CONFERENCE_TIME(conference_time) values ('18:00:00');
insert into CONFERENCE_TIME(conference_time) values ('22:00:00');

create table if not exists PAIR (
    id bigint auto_increment not null,
    group_id bigint not null,
    attendance_id bigint not null,

    primary key(id),
    unique key(group_id, attendance_id),
    foreign key(attendance_id) references ATTENDANCE(id) ON DELETE CASCADE
);
