USE `spring-data-test`;

CREATE TABLE user_accounts (
  user_id BIGINT NOT NULL PRIMARY KEY,
  balance BIGINT NOT NULL DEFAULT 0
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
