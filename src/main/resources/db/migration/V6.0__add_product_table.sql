create table product
(
    id bigint not null auto_increment primary key,
    created_date timestamp,
    last_modified_date timestamp,
    description varchar(100),
    product_status varchar(20)
) engine = InnoDB;