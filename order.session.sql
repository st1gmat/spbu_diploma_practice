-- ALTER SEQUENCE order_line_id_seq RESTART WITH 1;
-- ALTER SEQUENCE customer_order_id_seq RESTART WITH 1;
-- TRUNCATE TABLE order_line CASCADE;
-- TRUNCATE TABLE customer_order CASCADE;


DROP TABLE IF EXISTS order_line CASCADE;
DROP TABLE IF EXISTS customer_order CASCADE;
DROP TABLE IF EXISTS flyway_schema_history CASCADE;
DROP SEQUENCE IF EXISTS order_line_id_seq;
DROP SEQUENCE IF EXISTS customer_order_id_seq;
