CREATE TABLE IF NOT EXISTS payment (
    id SERIAL PRIMARY KEY,
    amount DECIMAL(38,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_last_modified_date()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_modified_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_payment_last_modified_date
BEFORE UPDATE ON payment
FOR EACH ROW
EXECUTE FUNCTION update_last_modified_date();
