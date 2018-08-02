CREATE DATABASE kanban;

\c kanban;

CREATE TABLE IF NOT EXISTS users (
  username VARCHAR(100) not null primary key,
  password VARCHAR(100) not null
);

INSERT INTO users (username, password) VALUES ('admin', '12345');
