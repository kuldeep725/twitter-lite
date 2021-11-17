package com.kunal.twitterlite.api;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserApi {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, Object> userMap) {
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");

        try {
            User user = userService.registerUser(username, password);
            Map<String, String> map = new HashMap<>();
            map.put("message", "registered succesfully");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (TwitterAuthException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed | " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, Object> userMap) {
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");

        User user = userService.validateUser(username, password);
        Map<String, String> map = new HashMap<>();
        map.put("message", "logged in succesfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    // For ADMIN user
    @GetMapping("/")
    public List<User> getAllUsers() {
        // TODO: check if the user is admin
        if(!userService.isAdmin(new User(null, "admin", null)))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be admin", new TwitterAuthException("User is not admin"));

        return userService.getAllUsers();
    }

}
