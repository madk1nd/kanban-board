CREATE DATABASE kanban;

\c kanban;

CREATE TABLE IF NOT EXISTS users (
  username VARCHAR(100) not null primary key,
  password VARCHAR(100) not null
);

INSERT INTO users (username, password) VALUES ('admin', '12345');

CREATE TABLE IF NOT EXISTS lists (
  id SERIAL not null primary key,
  ordinal INT not null,
  title VARCHAR(255) not null
);

INSERT INTO lists (ordinal, title) VALUES ('1', 'FirstList'), ('2', 'SecondList'), ('3', 'ThirdList');


CREATE TABLE IF NOT EXISTS tasks (

)