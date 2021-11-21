package com.kunal.twitterlite.api;

import com.kunal.twitterlite.service.FollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FollowingApi {

    @Autowired
    FollowingService followingService;

    @PostMapping("following/{followingId}")
    public ResponseEntity<Map<String, String>> followUser(@RequestAttribute UUID userId, @PathVariable UUID followingId) {
        followingService.followUser(userId, followingId);
        Map<String, String> map = new HashMap<>();
        map.put("status", "success");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("following")
    public List<Map<String, String>> getFollowings(@RequestAttribute UUID userId) {
        return followingService.findAllFollowings(userId);
    }

    @DeleteMapping("following/{followingId")
    public ResponseEntity<Map<String, String>> unfollowUser(@RequestAttribute UUID userId, @PathVariable UUID followingId) {
        followingService.unfollowUser(userId, followingId);
        Map<String, String> map = new HashMap<>();
        map.put("status", "success");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("follower")
    public List<Map<String, String>> getFollowers(@RequestAttribute UUID userId) {
        return followingService.findAllFollowers(userId);
    }

}
