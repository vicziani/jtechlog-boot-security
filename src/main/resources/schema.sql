create table users (id bigint generated by default as identity (start with 1),
    username varchar(255), password varchar(255), role varchar(255), primary key (id));
