package com.kunal.twitterlite.api;

import com.kunal.twitterlite.model.Post;
import com.kunal.twitterlite.model.PostDetail;
import com.kunal.twitterlite.model.User;
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
    public List<PostDetail> getPosts(@RequestAttribute UUID userId) {
        List<Post> posts = postService.getFollowingsPosts(userId);
        Collections.sort(posts);
        return posts.stream()
                .map(post -> {
                    User user = userService.findUserById(post.getUserId());
                    return postService.getPostDetails(post, user);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/personal")
    public List<PostDetail> getPersonalPosts(@RequestAttribute UUID userId) {
        List<Post> posts = postService.getPersonalPosts(userId);
        Collections.sort(posts);
        return posts.stream()
                .map(post -> {
                    User user = userService.findUserById(post.getUserId());
                    return postService.getPostDetails(post, user);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{postId}")
    public PostDetail getPost(@RequestAttribute UUID userId, @PathVariable UUID postId) {
        Post post = postService.getPost(postId);
        if(post == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check postId", null);

        User user = userService.findUserById(userId);
        return postService.getPostDetails(post, user);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Map<String, String>> likePost(@RequestAttribute UUID userId, @PathVariable UUID postId) {
        int status = postService.likePost(postId, userId);
        Map<String, String> map = new HashMap<>();

        map.put("status", status == 0 ? "success" : "failure");

        if(status == -1) {
            map.put("reason", "Bad request: check postId");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if(status == -2) {
            map.put("reason", "Post has already been liked");
            return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
    }

    @PostMapping("/{postId}/retweet")
    public ResponseEntity<Map<String, String>> retweetPost(@RequestAttribute UUID userId, @PathVariable UUID postId) {
        int status = postService.retweetPost(postId, userId);
        Map<String, String> map = new HashMap<>();

        map.put("status", status == 0 ? "success" : "failure");

        if(status == -1) {
            map.put("reason", "Bad request: check postId");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if(status == -2) {
            map.put("reason", "Post has already been retweeted");
            return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<Map<String, String>> deletePost(@RequestAttribute UUID userId, @PathVariable UUID postId) {
        int status = postService.deletePost(postId, userId);

        Map<String, String> map = new HashMap<>();

        map.put("status", status == 0 ? "success" : "failure");

        if(status == -1) {
            map.put("reason", "Bad request: check postId");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if(status == -2) {
            map.put("reason", "Post is not owned by you");
            return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{postId}/like")
    public ResponseEntity<Map<String, String>> unlikePost(@RequestAttribute UUID userId, @PathVariable UUID postId) {
        int status = postService.unlikePost(postId, userId);

        Map<String, String> map = new HashMap<>();

        map.put("status", status == 0 ? "success" : "failure");

        if(status == -1) {
            map.put("reason", "Bad request: check postId");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if(status == -2) {
            map.put("reason", "Post is already not liked");
            return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{postId}/retweet")
    public ResponseEntity<Map<String, String>> undoRetweet(@RequestAttribute UUID userId, @PathVariable UUID postId) {
        int status = postService.undoRetweet(postId, userId);

        Map<String, String> map = new HashMap<>();

        map.put("status", status == 0 ? "success" : "failure");

        if(status == -1) {
            map.put("reason", "Bad request: check postId");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        if(status == -2) {
            map.put("reason", "Post is already not retweeted");
            return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
    }

}
