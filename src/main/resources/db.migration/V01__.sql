CREATE TABLE IF NOT EXISTS accounts (
    id UUID NOT NULL,
    user_id VARCHAR NOT NULL,
    number VARCHAR NOT NULL,
    balance DECIMAL NOT NULL
);
