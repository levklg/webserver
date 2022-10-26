
create sequence hibernate_sequence start with 1 increment by 1;
create table clients
(
    id  bigserial not null primary key,
    name varchar(50),
    address_id bigserial not null
);
create table addresses
(
    id   bigserial not null primary key ,
    address varchar(255)


);

create table phones
(
    id  bigserial not null primary key,
    number varchar(255),
    client_id bigserial not null

);



