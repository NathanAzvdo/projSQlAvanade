--liquibase formatted sql
--changeset junior:1
--comment: Create table board

create table board (
    id serial primary key,
    name varchar(255) not null,
    description text,
    created_at timestamp not null default now(),
    updated_at timestamp
)ENGINE=InnoDB;

--rollback drop table board;