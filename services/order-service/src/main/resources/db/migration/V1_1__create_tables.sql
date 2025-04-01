CREATE TABLE customer_order (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(255),
    total_amount NUMERIC(19, 2),
    payment_method VARCHAR(50),
    customer_id VARCHAR(255),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_line (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,

    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES customer_order(id) ON DELETE CASCADE
);
