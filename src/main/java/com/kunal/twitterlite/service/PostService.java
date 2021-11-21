package com.kunal.twitterlite.service;

import com.kunal.twitterlite.model.Post;
import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    FollowingService followingService;

    public void createPost(UUID userId, String message) {
        postRepository.createPost(userId, message);
    }

    public List<Post> getFollowingsPosts(UUID userId) {
        List<User> followings = followingService.findAllFollowings(userId);
        return followings.stream()
                .flatMap(user -> postRepository.getPostsByUserId(user.getUserId()).stream())
                .collect(Collectors.toList());
    }

}
