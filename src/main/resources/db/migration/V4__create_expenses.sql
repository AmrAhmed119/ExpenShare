CREATE TABLE expenses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id BIGINT NOT NULL,
    paid_by BIGINT NOT NULL,
    amount DECIMAL(18, 2) NOT NULL CHECK (amount > 0),
    description VARCHAR(255) NOT NULL,
    split_type ENUM('EQUAL', 'EXACT', 'PERCENT') NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_expense_group FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE CASCADE,
    CONSTRAINT fk_expense_user FOREIGN KEY (paid_by) REFERENCES users(id)
);