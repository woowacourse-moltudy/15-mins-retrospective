create table if not exists member
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    password varchar(255) not null
);