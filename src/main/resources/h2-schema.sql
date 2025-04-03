drop table if exists order_header cascade;

create table order_header
(
    id                  bigint not null auto_increment primary key,
    customer            varchar(255),
    shipping_address    varchar(30),
    shipping_city       varchar(30),
    shipping_state      varchar(30),
    shipping_zip_code   varchar(30),
    bill_to_address     varchar(30),
    bill_to_city        varchar(30),
    bill_to_state       varchar(30),
    bill_to_zip_code    varchar(30)
);