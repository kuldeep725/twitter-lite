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
    posted_by UUID NOT NULL,
    replied_to UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_at TIMESTAMP WITH TIME ZONE NOT NULL,
    message VARCHAR (300) NOT NULL,
    CONSTRAINT fk_user_id FOREIGN KEY(posted_by) REFERENCES twitter_user(user_id)
);


CREATE TABLE likes(
    post_id UUID NOT NULL,
    liked_by UUID NOT NULL,
    liked_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY(post_id, liked_by),
    CONSTRAINT fk_post_id FOREIGN KEY(post_id) REFERENCES post(post_id),
    CONSTRAINT fk_user_id FOREIGN KEY(liked_by) REFERENCES twitter_user(user_id)
);

CREATE TABLE retweet(
     post_id UUID NOT NULL,
     retweeted_by UUID NOT NULL,
     retweeted_at TIMESTAMP WITH TIME ZONE,
     PRIMARY KEY(post_id, retweeted_by),
     CONSTRAINT fk_post_id FOREIGN KEY(post_id) REFERENCES post(post_id),
     CONSTRAINT fk_user_id FOREIGN KEY(retweeted_by) REFERENCES twitter_user(user_id)
);

-- dummy insertion of data
INSERT INTO twitter_user(username, password) VALUES('Williamson', '123');
INSERT INTO twitter_user(username, password) VALUES('Guptil', '123');
INSERT INTO twitter_user(username, password) VALUES('Neesham', '123');
INSERT INTO twitter_user(username, password) VALUES('Finch', '123');
INSERT INTO twitter_user(username, password) VALUES ('Warner', '123');
INSERT INTO twitter_user(username, password) VALUES ('Smith', '123');
