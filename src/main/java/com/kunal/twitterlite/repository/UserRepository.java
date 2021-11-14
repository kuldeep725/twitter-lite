package com.kunal.twitterlite.repository;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    int create(User user) throws TwitterAuthException;

    int getCountByUsername(String username);

    User findById(UUID userId);

    List<User> getAllUsers();

}
