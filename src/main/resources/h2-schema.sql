drop table if exists order_header cascade;
drop table if exists product cascade;
drop table if exists order_line cascade;

create table order_header
(
    id                  bigint not null auto_increment primary key,
    created_date        timestamp,
    last_modified_date  timestamp,
    customer            varchar(255),
    shipping_address    varchar(30),
    shipping_city       varchar(30),
    shipping_state      varchar(30),
    shipping_zip_code   varchar(30),
    bill_to_address     varchar(30),
    bill_to_city        varchar(30),
    bill_to_state       varchar(30),
    bill_to_zip_code    varchar(30),
    order_status        varchar(30)
);

create table product
(
    id bigint not null auto_increment primary key,
    created_date timestamp,
    last_modified_date timestamp,
    description varchar(100),
    product_status varchar(20)
);

create table order_line
(
    id bigint not null auto_increment primary key,
    quantity_ordered int,
    order_header_id bigint,
    created_date timestamp,
    last_modified_date timestamp,
    constraint order_header_pk FOREIGN KEY (order_header_id) references order_header(id)
);