CREATE TABLE IF NOT EXISTS conversations (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    conv_id          VARCHAR(255),
    from_user_id        BIGINT,
    from_user_name        VARCHAR(255),
    to_user_id          BIGINT,
    to_user_name          VARCHAR(255),
    time             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    content          VARCHAR(1023),
    delivery_status  VARCHAR(255)
);