create table members
(
    id          int auto_increment,
    name        varchar(500) not null,
    age         int          null,
    description text         null,
    constraint members_pk
        primary key (id)
);

