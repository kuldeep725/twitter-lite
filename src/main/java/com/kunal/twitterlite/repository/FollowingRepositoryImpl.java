package com.kunal.twitterlite.repository;

import com.kunal.twitterlite.exception.TwitterAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FollowingRepositoryImpl implements FollowingRepository {

    private static final String SQL_FOLLOW_USER_BY_ID = "INSERT INTO following(user_id, following_id) VALUES (?,?)";
    private static final String SQL_UNFOLLOW_USER_BY_ID = "DELETE FROM following WHERE user_id=? AND following_id=?";
    private static final String SQL_FIND_ALL_FOLLOWINGS = "SELECT following_id FROM following WHERE user_id = ?";
    private static final String SQL_FIND_ALL_FOLLOWERS = "SELECT user_id FROM following WHERE following_id = ?";
    private static final String SQL_IS_FOLLOWING = "SELECT COUNT(*) FROM following WHERE user_id=? AND following_id=?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    /*
        Returns: 0 if successful, -1 if failure in sql query
     */
    @Override
    public int followById(UUID userId, UUID followingId) {
        try {
            jdbcTemplate.update(SQL_FOLLOW_USER_BY_ID, userId, followingId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<UUID> findAllFollowings(UUID userId) {
        try {
            List<UUID> output = jdbcTemplate.query(SQL_FIND_ALL_FOLLOWINGS,
                ((rs, rowNum) -> UUID.fromString(rs.getString("following_id"))),
                userId
            );
            return output;
        } catch (Exception e) {
            throw new TwitterAuthException("Query failed | " + e);
        }
    }

    @Override
    public int unfollowById(UUID userId, UUID followingId) {
        try {
            jdbcTemplate.update(SQL_UNFOLLOW_USER_BY_ID, userId, followingId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<UUID> findAllFollowers(UUID userId) {
        try {
            List<UUID> output = jdbcTemplate.query(SQL_FIND_ALL_FOLLOWERS,
                ((rs, rowNum) -> UUID.fromString(rs.getString("user_id"))),
                userId
            );
            return output;
        } catch (Exception e) {
            throw new TwitterAuthException("Query failed | " + e);
        }
    }

    @Override
    public boolean isFollowing(UUID userId, UUID followingId) {
        return jdbcTemplate.queryForObject(SQL_IS_FOLLOWING, Integer.class, userId, followingId) != 0;
    }

}
