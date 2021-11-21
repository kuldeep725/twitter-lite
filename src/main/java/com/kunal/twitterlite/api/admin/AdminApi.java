package com.kunal.twitterlite.api.admin;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminApi {

    @Autowired
    UserService userService;

    @GetMapping
    public List<User> getAllUsers(@RequestAttribute UUID userId) {
        User user = userService.findUserById(userId);
        if(!userService.isAdmin(user))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be admin", new TwitterAuthException("User is not admin"));

        return userService.getAllUsers();
    }

}
