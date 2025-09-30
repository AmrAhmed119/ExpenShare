CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mobile_number VARCHAR(20),
    addr_line1 VARCHAR(150),
    addr_line2 VARCHAR(150),
    addr_city VARCHAR(80),
    addr_state VARCHAR(80),
    addr_postal VARCHAR(20),
    addr_country VARCHAR(2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);