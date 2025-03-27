--liquibase formatted sql

--changeset junior:fix_board
--comment: Remove columns title, description, created_at, updated_at from Board

ALTER TABLE board
DROP COLUMN title,
DROP COLUMN description,
DROP COLUMN created_at,
DROP COLUMN updated_at;

--rollback ALTER TABLE board ADD COLUMN title VARCHAR(255), ADD COLUMN description TEXT, ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT now(), ADD COLUMN updated_at TIMESTAMP;

--changeset junior:board_id_to_bigint
--comment: Alter Board id column to BIGINT

ALTER TABLE board MODIFY COLUMN id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT;

--rollback ALTER TABLE board MODIFY COLUMN id INT UNSIGNED NOT NULL AUTO_INCREMENT;

--changeset junior:2
--comment: Create table BoardColumn

CREATE TABLE board_column (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    column_order INT NOT NULL,
    kind VARCHAR(50) NOT NULL,
    board_id BIGINT UNSIGNED NOT NULL,
    CONSTRAINT fk_board_column_board FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE board_column;

--changeset junior:3
--comment: Create table Card

CREATE TABLE card (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    status ENUM('TODO', 'IN_PROGRESS', 'DONE', 'CANCELED', 'BLOCKED') NOT NULL,
    board_column_id BIGINT UNSIGNED NOT NULL,
    CONSTRAINT fk_card_board_column FOREIGN KEY (board_column_id) REFERENCES board_column(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE card;

--changeset junior:4
--comment: Create table CardHistory

CREATE TABLE card_history (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT UNSIGNED NOT NULL,
    board_column_id BIGINT UNSIGNED NOT NULL,
    entered_at TIMESTAMP NOT NULL,
    exited_at TIMESTAMP NULL,
    CONSTRAINT fk_card_history_card FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE,
    CONSTRAINT fk_card_history_board_column FOREIGN KEY (board_column_id) REFERENCES board_column(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE card_history;

--changeset junior:5
--comment: Create table Block

CREATE TABLE block (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT UNSIGNED NOT NULL,
    board_column_id BIGINT UNSIGNED NOT NULL,
    block_cause TEXT NOT NULL,
    block_in TIMESTAMP NOT NULL,
    unblock_cause TEXT,
    unblock_in TIMESTAMP NULL,
    CONSTRAINT fk_block_card FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE,
    CONSTRAINT fk_block_board_column FOREIGN KEY (board_column_id) REFERENCES board_column(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE block;
