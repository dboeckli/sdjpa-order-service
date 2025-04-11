alter table customer
    add column version integer;

update customer
set version = 0 where version is null;

alter table customer
    modify column version integer not null;