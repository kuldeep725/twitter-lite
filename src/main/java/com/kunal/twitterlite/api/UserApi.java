package com.kunal.twitterlite.api;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApi {

    @Autowired
    private UserService userService;

    public void createUser(User user) {

    }

    // For ADMIN user
    @GetMapping("/users")
    public List<User> getAllUsers() {
        // TODO: check if the user is admin
        if(!userService.isAdmin(new User(null, "admin", null)))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be admin", new TwitterAuthException("User is not admin"));

        return userService.getAllUsers();
    }

}
