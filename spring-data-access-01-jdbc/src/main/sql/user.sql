CREATE DATABASE `spring-data-test`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE `spring-data-test`;

CREATE TABLE users (
  id       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name     VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
