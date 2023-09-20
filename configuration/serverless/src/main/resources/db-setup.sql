create table table_entity (
    id bigint not null,
    max_seats integer,
    primary key (id)
);

insert into table_entity(id, max_seats) values
(100, 2),
(102, 2),
(103, 4),
(104, 5),
(105, 6),
(106, 6),
(107, 8),
(108, 12);
