package com.kunal.twitterlite.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FollowingRepository {

//    int followUserByUsername(String username);

    int followById(UUID userId, UUID followingId);

    List<UUID> findAllFollowings(UUID userId);

    int unfollowById(UUID userId, UUID followingId);

    List<UUID> findAllFollowers(UUID userId);

    boolean isFollowing(UUID userId, UUID followingId);
}
