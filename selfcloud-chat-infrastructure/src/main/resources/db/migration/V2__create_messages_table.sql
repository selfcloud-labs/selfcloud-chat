CREATE TABLE messages (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    content               TEXT,
    time                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delivery_status       TEXT,
    from_user_name        VARCHAR(255),
    conv_id               VARCHAR(255),

    CONSTRAINT fk_conv_id FOREIGN KEY (conv_id) REFERENCES conversations(conv_id)
);
