DROP DATABASE IF EXISTS twitterdb;
CREATE DATABASE twitterdb;
\c twitterdb;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE twitter_user(
    user_id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) NOT NULL,
    password VARCHAR(200) NOT NULL
);

CREATE TABLE following(
    user_id UUID NOT NULL,
    following_id UUID NOT NULL,
    PRIMARY KEY(user_id, following_id),
    CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES twitter_user(user_id),
    CONSTRAINT fk_following_id FOREIGN KEY(following_id) REFERENCES twitter_user(user_id)
);

CREATE TABLE post(
    post_id UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    created_on TIMESTAMP WITH TIME ZONE NOT NULL,
    message VARCHAR (300) NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES twitter_user(user_id)
);

INSERT INTO twitter_user(username, password) VALUES('Williamson', '123');
INSERT INTO twitter_user(username, password) VALUES('Guptil', '123');
INSERT INTO twitter_user(username, password) VALUES('Neesham', '123');
INSERT INTO twitter_user(username, password) VALUES('Finch', '123');
INSERT INTO twitter_user(username, password) VALUES ('Warner', '123');
INSERT INTO twitter_user(username, password) VALUES ('Smith', '123');