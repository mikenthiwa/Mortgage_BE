CREATE TABLE mortgage_applications (
    id SERIAL PRIMARY KEY,
    applicant_name VARCHAR(100) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    user_id INTEGER REFERENCES users(id)
);