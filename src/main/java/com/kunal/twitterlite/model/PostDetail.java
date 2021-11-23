package com.kunal.twitterlite.model;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class PostDetail {

    private UUID postId;
    private UUID userId;
    private String username;
    private String message;
    private Date createdOn;
    private UUID repliedTo;

    private int likesCount;
    private int retweetCount;

}
