CREATE TABLE conversations (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    conv_id             VARCHAR(255) UNIQUE,
    from_user_name      VARCHAR(255),
    to_user_name        VARCHAR(255),
    status              VARCHAR(255)
);
