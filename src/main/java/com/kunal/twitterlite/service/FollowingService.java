package com.kunal.twitterlite.service;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;
import com.kunal.twitterlite.repository.FollowingRepository;
import com.kunal.twitterlite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FollowingService {

    @Autowired
    FollowingRepository followingRepo;
    @Autowired
    UserRepository userRepository;

    public int followUser(UUID userId, UUID followingId) throws TwitterAuthException {
        if(followingRepo.isFollowing(userId, followingId)) {
            System.err.println("USER with userId " + userId + " is already following USER with userId " + followingId);
            return -2;
        }
        return followingRepo.followById(userId, followingId);
    }

    public List<User> findAllFollowings(UUID userId) {
        return followingRepo.findAllFollowings(userId)
                .stream()
                .map(id -> userRepository.findById(id))
                .collect(Collectors.toList());
    }

    public List<User> findAllFollowers(UUID userId) {
        return followingRepo.findAllFollowers(userId)
                .stream()
                .map(id -> userRepository.findById(id))
                .collect(Collectors.toList());
    }

    public int unfollowUser(UUID userId, UUID followingId) {
        if(!followingRepo.isFollowing(userId, followingId)) {
            System.err.println("USER with userId " + userId + " is not following USER with userId " + followingId);
            return -2;
        }
        return followingRepo.unfollowById(userId, followingId);    }

}
