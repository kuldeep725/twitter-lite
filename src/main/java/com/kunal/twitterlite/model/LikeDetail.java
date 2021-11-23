package com.kunal.twitterlite.model;

import lombok.Data;
import lombok.NonNull;

import java.util.Date;
import java.util.UUID;

@Data
public class LikeDetail implements Comparable<LikeDetail>{
    private UUID postId;
    private UUID likedBy;
    private Date likedAt;

    @Override
    public int compareTo(@NonNull LikeDetail other) {
        if(this.getLikedAt() == null || other.getLikedAt() == null)
            return 0;

        return other.getLikedAt().compareTo(this.getLikedAt());
    }
}
