drop table if exists post CASCADE;

create table post (
    id bigint not null auto_increment,
    title varchar(255),
    content varchar(255),
    category varchar(255),
    writer varchar(255),
    primary key (id)
);