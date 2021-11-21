package com.kunal.twitterlite.service;

import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.repository.FollowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FollowingService {

    @Autowired
    FollowingRepository followingRepo;

    public void followUser(UUID userId, UUID followingId) {
        followingRepo.followUserById(userId, followingId);
    }

    public List<Map<String, String>> findAllFollowings(UUID userId) {
        return followingRepo.findAllFollowings(userId);
    }

    public List<Map<String, String>> findAllFollowers(UUID userId) {
        return followingRepo.findAllFollowers(userId);
    }

    public void unfollowUser(UUID userId, UUID followingId) {
        followingRepo.unfollowUserById(userId, followingId);
    }

}
