package com.kunal.twitterlite.model;

import org.springframework.lang.NonNull;

import java.util.*;

public class Post implements Comparable<Post>{

    private UUID postId;
    private UUID userId;
    private String message;
    private Date createdAt;
    private UUID repliedTo;

    public Post(UUID postId, UUID userId, String message, Date createdAt, UUID repliedTo) {
        this.postId = postId;
        this.userId = userId;
        this.message = message;
        this.createdAt = createdAt;
        this.repliedTo = repliedTo;
    }

    public UUID getRepliedTo() {
        return repliedTo;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", repliedTo=" + repliedTo +
                '}';
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UUID getPostId() {
        return postId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int compareTo(@NonNull Post other) {
        if(this.getCreatedAt() == null || other.getCreatedAt() == null)
            return 0;
        // sort in descending order of time of creation
        return other.getCreatedAt().compareTo(this.getCreatedAt());
    }

    // Obsolete
    public Map<String, String> getPostMap() {
        Map<String, String> map = new HashMap<>();
        // Using reflection api to automate the map generation of Post class
        Arrays.stream(Post.class.getDeclaredFields()).forEach(field -> {
            try {
                map.put(field.getName(), Optional.ofNullable(field.get(this)).orElse("null").toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return map;
    }
}
