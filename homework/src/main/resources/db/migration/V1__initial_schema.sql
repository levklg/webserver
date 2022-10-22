
create sequence hibernate_sequence start with 1 increment by 1;
create table clients
(
    id    bigserial not null primary key,
    name varchar(50)

  );

create table phones
(
    id  bigint not null primary key,
    number varchar(255),
    client_id bigint

);



