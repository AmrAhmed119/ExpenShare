CREATE TABLE settlements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    expense_id BIGINT NOT NULL,
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NOT NULL,
    amount DECIMAL(18,2) NOT NULL CHECK (amount > 0),
    method ENUM('CASH', 'BANK_TRANSFER', 'WALLET', 'OTHER') DEFAULT 'OTHER',
    note VARCHAR(255),
    reference VARCHAR(64),
    status ENUM('PENDING', 'CONFIRMED', 'CANCELED') DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL,
    confirmed_at TIMESTAMP,
    CONSTRAINT fk_settlements_expense FOREIGN KEY (expense_id) REFERENCES expenses(id),
    CONSTRAINT fk_settlements_from_user FOREIGN KEY (from_user_id) REFERENCES users(id),
    CONSTRAINT fk_settlements_to_user FOREIGN KEY (to_user_id) REFERENCES users(id)
);