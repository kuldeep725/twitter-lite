package com.kunal.twitterlite.service;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public User registerUser(String username, String password) {
        if(repo.getCountByUsername(username) != 0)
            throw new TwitterAuthException("Duplicate username");

        UUID userId = repo.create(username, password);
        return repo.findById(userId);
    }

    public boolean isAdmin(User user) {
        return user.getUsername().equals("admin");
    }

    public List<User> getAllUsers() {
        return repo.getAllUsers();
    }

    public User validateUser(String username, String password) {
        return repo.findByUsernameAndPassword(username, password);
    }
}
