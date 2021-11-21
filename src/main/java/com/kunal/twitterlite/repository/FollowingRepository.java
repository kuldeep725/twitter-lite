package com.kunal.twitterlite.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FollowingRepository {

//    int followUserByUsername(String username);

    int followUserById(UUID userId, UUID followingId);

    List<Map<String, String>> findAllFollowings(UUID userId);

    int unfollowUserById(UUID userId, UUID followingId);

    List<Map<String, String>> findAllFollowers(UUID userId);
}
