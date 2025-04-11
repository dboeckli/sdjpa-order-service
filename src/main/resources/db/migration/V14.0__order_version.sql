alter table order_header
    add column version integer;

alter table order_line
    add column version integer;

update order_header
set version = 0 where version is null;

update order_line
set version = 0 where version is null;

alter table order_header
    modify column version integer not null;

alter table order_line
    modify column version integer not null;