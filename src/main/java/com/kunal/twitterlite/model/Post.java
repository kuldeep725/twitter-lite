package com.kunal.twitterlite.model;

import java.util.*;

public class Post implements Comparable<Post>{

    private UUID postId;
    private UUID userId;
    private String message;
    private Date createdOn;

    public Post(UUID postId, UUID userId, String message, Date createdOn) {
        this.postId = postId;
        this.userId = userId;
        this.message = message;
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }

    public Date getCreatedOn() {
        return createdOn;
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
    public int compareTo(Post o) {
        if(this.getCreatedOn() == null || o.getCreatedOn() == null)
            return 0;
        // sort in descending order of time of creation
        return o.getCreatedOn().compareTo(this.getCreatedOn());
    }

    public Map<String, String> getPostMap() {
        Map<String, String> map = new HashMap<>();
        // Using reflection api to automate the map generation of Post class
        Arrays.stream(Post.class.getDeclaredFields()).forEach(field -> {
                try {
                    map.put(field.getName(), field.get(this).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        );
        return map;
    }
}
