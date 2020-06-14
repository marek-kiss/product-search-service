CREATE TABLE IF NOT EXISTS product (
  id integer primary key,
  name varchar,
  price numeric,
  brand varchar,
  on_sale boolean
);

CREATE SEQUENCE IF NOT EXISTS product_id_seq START 1;

CREATE INDEX ON product (brand);