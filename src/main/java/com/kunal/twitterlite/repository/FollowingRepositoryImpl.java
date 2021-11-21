package com.kunal.twitterlite.repository;

import com.kunal.twitterlite.exception.TwitterAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FollowingRepositoryImpl implements FollowingRepository {

    private static final String SQL_FOLLOW_USER_BY_ID = "INSERT INTO following(user_id, following_id) VALUES (?,?)";
    private static final String SQL_UNFOLLOW_USER_BY_ID = "DELETE FROM following WHERE user_id=? and following_id=?";
    private static final String SQL_FIND_ALL_FOLLOWINGS = "SELECT f.following_id followingId, t.username followingName\n" +
            "FROM following f JOIN twitter_user t\n" +
            "ON f.following_id = t.user_id\n" +
            "WHERE f.user_id = ?";
    private static final String SQL_FIND_ALL_FOLLOWERS = "SELECT f.user_id followerId, t.username followerName\n" +
            "FROM following f\n" +
            "JOIN twitter_user t ON f.user_id = t.user_id\n" +
            "WHERE f.following_id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int followUserById(UUID userId, UUID followingId) {
        try {
            jdbcTemplate.update(SQL_FOLLOW_USER_BY_ID, userId, followingId);
            return 0;
        } catch (Exception e) {
            throw new TwitterAuthException("Follow request failed | " + e);
        }
    }

    @Override
    public List<Map<String, String>> findAllFollowings(UUID userId) {
        try {
            List<Map<String, String>> output = jdbcTemplate.query(SQL_FIND_ALL_FOLLOWINGS, ((rs, rowNum) -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("followingId", rs.getString("followingId"));
                    map.put("followingName", rs.getString("followingName"));
                    return map;
                }),
                userId
            );
            return output;
        } catch (Exception e) {
            throw new TwitterAuthException("Query failed | " + e);
        }
    }

    @Override
    public int unfollowUserById(UUID userId, UUID followingId) {
        try {
            jdbcTemplate.update(SQL_UNFOLLOW_USER_BY_ID, userId, followingId);
            return 0;
        } catch (Exception e) {
            throw new TwitterAuthException("Unfollow request failed | " + e);
        }
    }

    @Override
    public List<Map<String, String>> findAllFollowers(UUID userId) {
        try {
            List<Map<String, String>> output = jdbcTemplate.query(SQL_FIND_ALL_FOLLOWERS, ((rs, rowNum) -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("followerId", rs.getString("followerId"));
                    map.put("followerName", rs.getString("followerName"));
                    return map;
                }),
                userId
            );
            return output;
        } catch (Exception e) {
            throw new TwitterAuthException("Query failed | " + e);
        }
    }

}
