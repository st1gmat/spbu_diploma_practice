CREATE TABLE customer_order (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(255),
    total_amount NUMERIC(19, 2),
    payment_method VARCHAR(255),
    customer_id VARCHAR(255),
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE order_line (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES customer_order(id),
    product_id INTEGER,
    quantity DOUBLE PRECISION
);
