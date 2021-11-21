package com.kunal.twitterlite.api;

import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.service.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FollowingApi {

    @Autowired
    FollowingService followingService;

    @PostMapping("/following/{followingId}")
    public ResponseEntity<Map<String, String>> followUser(@RequestAttribute UUID userId, @PathVariable UUID followingId) {
        int status = followingService.followUser(userId, followingId);
        Map<String, String> map = new HashMap<>();

        map.put("status", status == 0 ? "success" : "failure");

        if(status == -1) {
            map.put("reason", "Bad request: check followingId");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if(status == -2) {
            map.put("reason", "USER with userId " + userId + " is already following USER with userId " + followingId);
            return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/following")
    public List<Map<String, String>> getFollowings(@RequestAttribute UUID userId) {
        List<User> followings = followingService.findAllFollowings(userId);
        return followings.stream()
                .map(following -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("followingId", following.getUserId().toString());
                    map.put("followingName", following.getUsername());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @DeleteMapping("/following/{followingId}")
    public ResponseEntity<Map<String, String>> unfollowUser(@RequestAttribute UUID userId, @PathVariable UUID followingId) {
        int status = followingService.unfollowUser(userId, followingId);
        Map<String, String> map = new HashMap<>();

        map.put("status", status == 0 ? "success" : "failure");

        if(status == -1) {
            map.put("reason", "Bad request: check followingId");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if(status == -2) {
            map.put("reason", "USER with userId " + userId + " is not following USER with userId " + followingId);
            return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/follower")
    public List<Map<String, String>> getFollowers(@RequestAttribute UUID userId) {
        List<User> followers = followingService.findAllFollowers(userId);
        return followers.stream()
                .map(follower -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("followerId", follower.getUserId().toString());
                    map.put("followerName", follower.getUsername());
                    return map;
                })
                .collect(Collectors.toList());
    }

}
