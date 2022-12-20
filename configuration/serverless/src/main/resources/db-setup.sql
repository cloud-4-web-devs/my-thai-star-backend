create table table_entity (
    id bigint not null,
    max_seats integer,
    primary key (id)
);

insert into table_entity(id, max_seats)
values (100, 2),
values (102, 2),
values (103, 4),
values (104, 5),
values (105, 6),
values (106, 6),
values (107, 8),
values (108, 12);
