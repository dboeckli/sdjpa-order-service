alter table order_header
    add column version integer;

alter table order_line
    add column version integer;

update order_header
set version = 0 where version = null;

update order_line
set version = 0 where version = null;