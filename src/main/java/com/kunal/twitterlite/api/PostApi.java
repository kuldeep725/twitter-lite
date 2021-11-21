package com.kunal.twitterlite.api;

import com.kunal.twitterlite.model.Post;
import com.kunal.twitterlite.service.PostService;
import com.kunal.twitterlite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
public class PostApi {
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createPost(@RequestAttribute UUID userId, @RequestBody Map<String, Object> postMap) {
        String message = (String) postMap.get("message");
        if(message == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message property is missing from request body", null);

        if(message.length() >= 300)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message length can not be more than 200 characters", null);

        postService.createPost(userId, message);
        Map<String, String> map = new HashMap<>();
        map.put("status", "success");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Map<String, String>> getPosts(@RequestAttribute UUID userId) {
        List<Post> posts = postService.getFollowingsPosts(userId);
        Collections.sort(posts);
        return posts.stream()
                .map(post -> {
                    Map<String, String> map = post.getPostMap();
                    String username  = userService.findUserById(post.getUserId()).getUsername();
                    map.put("username", username);
                    return map;
                })
                .collect(Collectors.toList());
    }

}
