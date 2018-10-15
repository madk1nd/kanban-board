CREATE DATABASE kanban;

\c kanban;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  username VARCHAR(100) not null,
  password VARCHAR(100) not null,
  name VARCHAR(100) not null,
  enabled BOOLEAN not null default false
);
ALTER TABLE users ADD UNIQUE (username);
CREATE INDEX ON users (username);

INSERT INTO users (username, password, name) VALUES ('admin', '$2a$10$SY2YXk2TG9N9B0w9OTWWsua3lBSB.my/OMNJZWxIF36N3eyodOIlK', 'Simple name');

CREATE TABLE IF NOT EXISTS tokens (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID not null,
  remote_ip VARCHAR(15) not null,
  token VARCHAR(255) not null,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO tokens (user_id, token) VALUES ('425fa018-b8e7-4187-b566-8141d4934c4e', 'token');

CREATE TABLE IF NOT EXISTS validation (
  token VARCHAR(255) PRIMARY KEY NOT NULL,
  username VARCHAR(100) not null
);