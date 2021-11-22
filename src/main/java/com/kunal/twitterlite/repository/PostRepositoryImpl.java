package com.kunal.twitterlite.repository;

import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class PostRepositoryImpl implements PostRepository{

    private static final String SQL_CREATE_POST = "INSERT INTO post(posted_by, message, created_on) VALUES (?, ?, ?)";
    private static final String SQL_FIND_POSTS_BY_USERID = "SELECT * FROM post WHERE posted_by = ? AND replied_to IS NULL";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int createPost(UUID postedBy, String message) throws TwitterAuthException{
        try {
            Date createdOn = Calendar.getInstance().getTime();
            jdbcTemplate.update(SQL_CREATE_POST, postedBy, message, createdOn);
            return 0;
        } catch (Exception e) {
            throw new TwitterAuthException("Post creation failed | " + e);
        }
    }

    @Override
    public List<Post> getPostsByUserId(UUID postedBy) {
        return jdbcTemplate.query(SQL_FIND_POSTS_BY_USERID, postMapper,
            postedBy
        );
    }

    private final RowMapper<Post> postMapper =  (rs, rowNum) -> new Post(
            UUID.fromString(rs.getString("post_id")),
            UUID.fromString(rs.getString("posted_by")),
            rs.getString("message"),
            rs.getTimestamp("created_on"),
            (UUID) rs.getObject("replied_to")
    );
}
