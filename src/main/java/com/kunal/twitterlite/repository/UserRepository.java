package com.kunal.twitterlite.repository;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    UUID create(String username, String password) throws TwitterAuthException;

    int getCountByUsername(String username);

    User findById(UUID userId);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password) throws TwitterAuthException;

    List<User> getAllUsers();

}
