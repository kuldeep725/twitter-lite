package com.kunal.twitterlite.api;

import com.kunal.twitterlite.Constant;
import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = { "http://localhost:3000/", "https://twitter-lite-frontend.herokuapp.com"})
public class UserApi {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> userMap) {
        String username = (String) userMap.get("username");
        String fullname = (String) userMap.get("fullname");
        String password = (String) userMap.get("password");

        if(username == null || fullname == null || password == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed", null);


        try {
            User user = userService.registerUser(username, fullname, password);
            return new ResponseEntity<>(generateJWTToken(user), HttpStatus.CREATED);
        } catch (TwitterAuthException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed | " + e.getMessage(), e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> userMap) {
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");

        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed", null);

        User user;
        try {
            user = userService.validateUser(username, password);
        } catch (TwitterAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        }
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    private Map<String, Object> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constant.SECRET_API_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constant.TOKEN_VALIDITY))
                .claim("userId", user.getUserId())
                .claim("username", user.getUsername())
                .compact();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.getUsername());
        userMap.put("userId", user.getUserId());
        userMap.put("fullname", user.getFullname());

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", userMap);

        return map;
    }

}
