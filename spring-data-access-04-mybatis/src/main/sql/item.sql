CREATE TABLE item (
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    item_name VARCHAR(255) NOT NULL,
    price    INT         NOT NULL,
    quantity INT         NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
