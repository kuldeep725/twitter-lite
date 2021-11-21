package com.kunal.twitterlite.repository;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class PostRepositoryImpl implements PostRepository{

    private static final String SQL_CREATE_POST = "INSERT INTO post(user_id, message, created_on) VALUES (?, ?, ?)";
    private static final String SQL_FIND_POSTS_BY_USERID = "SELECT * FROM post WHERE user_id = ?";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int createPost(UUID userId, String message) throws TwitterAuthException{
        try {
            Date createdOn = Calendar.getInstance().getTime();
            jdbcTemplate.update(SQL_CREATE_POST, userId, message, createdOn);
            return 0;
        } catch (Exception e) {
            throw new TwitterAuthException("Post creation failed | " + e);
        }
    }

    @Override
    public List<Post> getPostsByUserId(UUID userId) {
        return jdbcTemplate.query(SQL_FIND_POSTS_BY_USERID, (rs, rowNum) -> new Post(
                UUID.fromString(rs.getString("post_id")),
                UUID.fromString(rs.getString("user_id")),
                rs.getString("message"),
                rs.getTimestamp("created_on")
            ),
            userId
        );
    }
}
