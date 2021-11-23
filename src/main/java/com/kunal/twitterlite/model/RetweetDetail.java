package com.kunal.twitterlite.model;

import lombok.Data;
import lombok.NonNull;

import java.util.Date;
import java.util.UUID;

@Data
public class RetweetDetail implements Comparable<RetweetDetail>{
    private UUID postId;
    private UUID retweetedBy;
    private Date retweetAt;

    @Override
    public int compareTo(@NonNull RetweetDetail other) {
        if(this.getRetweetAt() == null || other.getRetweetAt() == null)
            return 0;

        return other.getRetweetAt().compareTo(this.getRetweetAt());
    }
}
