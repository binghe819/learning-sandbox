create table if not exists customers (
       id bigint not null auto_increment,
       name varchar(255) not null,
       balance decimal(15, 2) not null,
       primary key (id)
)
