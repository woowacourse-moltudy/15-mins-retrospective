create table if not exists temp
(
    id bigint auto_increment not null,
    name varchar(255) not null unique
);
