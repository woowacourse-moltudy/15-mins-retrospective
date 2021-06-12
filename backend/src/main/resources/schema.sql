create table if not exists MEMBER (
    id bigint auto_increment not null,
    name varchar(255) not null unique
);

create table if not exists CONFERENCE_TIME (
    id bigint auto_increment not null,
    time int(24) not null unique
);

create table if not exists ATTENDANCE (
    id bigint auto_increment not null,
    day TIMESTAMP DEFAULT FORMATDATETIME(NOW(), 'yyyy-MM-dd'),
    member_id bigint not null,
    time_id bigint not null,

    primary key(id),

    foreign key(member_id) references MEMBER(id),
    foreign key(time_id) references CONFERENCE_TIME(id)
);

insert into CONFERENCE_TIME(time) values (6);
insert into CONFERENCE_TIME(time) values (10);
