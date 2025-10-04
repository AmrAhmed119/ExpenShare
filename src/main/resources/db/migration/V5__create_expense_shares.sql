CREATE TABLE expense_shares (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    expense_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    shared_amount DECIMAL(18, 2) NOT NULL,
    CONSTRAINT fk_share_expense FOREIGN KEY (expense_id) REFERENCES expenses(id) ON DELETE CASCADE,
    CONSTRAINT fk_share_user FOREIGN KEY (user_id) REFERENCES users(id)
);