package com.kunal.twitterlite.model;

import java.util.UUID;

public class User {

    private UUID userId;
    private String username;
    private String password;

    public User(UUID user_id, String username, String password) {
        this.userId = user_id;
        this.username = username;
        this.password = password;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
