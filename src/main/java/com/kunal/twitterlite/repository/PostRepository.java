package com.kunal.twitterlite.repository;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostRepository {

    int createPost(UUID userId, String message) throws TwitterAuthException;

    int createPost(UUID postedBy, String message, UUID repliedTo) throws TwitterAuthException;

    List<Post> findPostsById(UUID userId);

    Post findPost(UUID postId);

    int likePost(UUID postId, UUID likedBy);

    int retweetPost(UUID postId, UUID retweetedBy);

    List<UUID> findPostLikes(UUID postId);

    List<UUID> findPostRetweets(UUID postId);

    int deletePost(UUID postId);

    int unlikePost(UUID postId, UUID likedBy);

    int undoRetweetPost(UUID postId, UUID retweetedBy);

    List<Post> findComments(UUID repliedTo);
}
