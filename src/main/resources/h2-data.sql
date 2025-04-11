insert into product (description, product_status, created_date, last_modified_date, quantity_on_hand)
values ('PRODUCT1', 'NEW', now(), now(), 0);

insert into product (description, product_status, created_date, last_modified_date, quantity_on_hand)
values ('PRODUCT2', 'NEW', now(), now(), 0);

insert into product (description, product_status, created_date, last_modified_date, quantity_on_hand)
values ('PRODUCT3', 'NEW', now(), now(), 0);

insert into product (description, product_status, created_date, last_modified_date, quantity_on_hand)
values ('PRODUCT4', 'NEW', now(), now(), 0);

insert into category (description, created_date, last_modified_date) VALUES
    ('CAT1', now(), now());

insert into category (description, created_date, last_modified_date) VALUES
    ('CAT2', now(), now());

insert into category (description, created_date, last_modified_date) VALUES
    ('CAT3', now(), now());

insert into product_category (product_id, category_id)
SELECT p.id, c.id FROM product p, category c
where p.description = 'PRODUCT1' and c.description = 'CAT1';

insert into product_category (product_id, category_id)
SELECT p.id, c.id FROM product p, category c
where p.description = 'PRODUCT2' and c.description = 'CAT1';

insert into product_category (product_id, category_id)
SELECT p.id, c.id FROM product p, category c
where p.description = 'PRODUCT1' and c.description = 'CAT3';

insert into product_category (product_id, category_id)
SELECT p.id, c.id FROM product p, category c
where p.description = 'PRODUCT4' and c.description = 'CAT3';

insert into customer (customer_name, address, city, state, zip_code, phone, email, version)
values ('Customer 1', '123 Duval', 'Key West', 'FL', '33040', '305.292.1435',
        'cheeseburger@margaritville.com', 0 );

update order_header set order_header.customer_id = (select id from customer limit 1);