create table members
(
    id          int auto_increment,
    name        varchar(500) not null,
    age         int          null,
    description mediumtext    null,
    constraint members_pk
        primary key (id)
);

