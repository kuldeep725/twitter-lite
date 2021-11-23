package com.kunal.twitterlite.service;

import com.kunal.twitterlite.model.Post;
import com.kunal.twitterlite.model.PostDetail;
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
                .flatMap(user -> postRepository.findPostsById(user.getUserId()).stream())
                .collect(Collectors.toList());
    }

    public List<Post> getPersonalPosts(UUID userId) {
        return postRepository.findPostsById(userId);
    }

    public Post getPost(UUID postId) {
        return postRepository.findPost(postId);
    }

    public int likePost(UUID postId, UUID likedBy) {
        if(postRepository.findPostLikes(postId).contains(likedBy))
            return -2;

        return postRepository.likePost(postId, likedBy);
    }

    public int retweetPost(UUID postId, UUID retweetedBy) {
        if(postRepository.findPostRetweets(postId).contains(retweetedBy))
            return -2;

        return postRepository.retweetPost(postId, retweetedBy);
    }

    public List<UUID> getLikes(UUID postId) {
        return postRepository.findPostLikes(postId);
    }

    public List<UUID> getRetweets(UUID postId) {
        return postRepository.findPostRetweets(postId);
    }

    public List<Post> getComments(UUID postId) {
        return postRepository.findComments(postId);
    }

    public PostDetail getPostDetails(Post post, User user) {

        UUID postId = post.getPostId();

        PostDetail postDetail = new PostDetail();
        postDetail.setPostId(postId);
        postDetail.setUserId(post.getUserId());
        postDetail.setUsername(user.getUsername());
        postDetail.setMessage(post.getMessage());
        postDetail.setCreatedOn(post.getCreatedOn());
        postDetail.setRepliedTo(post.getRepliedTo());

        postDetail.setLikes(getLikes(postId).size());
        postDetail.setRetweets(getRetweets(postId).size());
        postDetail.setComments(getComments(postId).size());

        return postDetail;
    }

    public int deletePost(UUID postId, UUID userId) {
        Post post = postRepository.findPost(postId);
        if(post == null)
            return -1;
        if(!post.getUserId().equals(userId))
            return -2;

        return postRepository.deletePost(postId);
    }

    public int unlikePost(UUID postId, UUID userId) {
        if(!postRepository.findPostLikes(postId).contains(userId))
            return -2;

        return postRepository.unlikePost(postId, userId);
    }

    public int undoRetweet(UUID postId, UUID userId) {
        if(!postRepository.findPostRetweets(postId).contains(userId))
            return -2;

        return postRepository.undoRetweetPost(postId, userId);
    }

    public int addComment(UUID postId, UUID userId, String message) {
        if(postRepository.findPost(postId) == null)
            return -1;

        return postRepository.createPost(userId, message, postId);
    }
}
