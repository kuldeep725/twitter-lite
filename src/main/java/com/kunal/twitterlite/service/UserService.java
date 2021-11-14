package com.kunal.twitterlite.service;

import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public User createUser() {
        return null;
    }

    public boolean isAdmin(User user) {
        return user.getUsername().equals("admin");
    }

    public List<User> getAllUsers() {
        return repo.getAllUsers();
    }

}
