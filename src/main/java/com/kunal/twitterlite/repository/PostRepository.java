package com.kunal.twitterlite.repository;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostRepository {

    int createPost(UUID userId, String message) throws TwitterAuthException;

    List<Post> getPostsByUserId(UUID userId);

}
