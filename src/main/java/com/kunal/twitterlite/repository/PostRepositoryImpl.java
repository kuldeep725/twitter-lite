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
    private static final String SQL_FIND_POST = "SELECT * FROM post WHERE post_id = ?";
    private static final String SQL_LIKE_POST = "INSERT INTO likes(post_id, liked_by) VALUES (?, ?)";
    private static final String SQL_RETWEET_POST = "INSERT INTO retweet(post_id, retweeted_by) VALUES (?, ?)";
    private static final String SQL_LIKED_BY = "SELECT liked_by FROM likes WHERE post_id = ?";
    private static final String SQL_RETWEETED_BY = "SELECT retweeted_by FROM retweet WHERE post_id = ?";

    private static final String SQL_DELETE_POST = "DELETE FROM post WHERE post_id = ?";
    private static final String SQL_UNLIKE_POST = "DELETE FROM likes WHERE post_id = ? AND liked_by = ?";
    private static final String SQL_UNDO_RETWEET_POST = "DELETE FROM retweet WHERE post_id = ? AND retweeted_by = ?";

    private static final String SQL_ADD_COMMENT = "INSERT INTO post(posted_by, message, created_on, replied_to) VALUES(?, ?, ?, ?)";
    private static final String SQL_FIND_COMMENTS = "SELECT * FROM post WHERE replied_to = ?";
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
    public int createPost(UUID postedBy, String message, UUID repliedTo) throws TwitterAuthException{
        try {
            Date createdOn = Calendar.getInstance().getTime();
            jdbcTemplate.update(SQL_ADD_COMMENT, postedBy, message, createdOn, repliedTo);
            return 0;
        } catch (Exception e) {
            throw new TwitterAuthException("Comment failed failed | " + e);
        }
    }

    @Override
    public List<Post> findPostsById(UUID postedBy) {
        return jdbcTemplate.query(SQL_FIND_POSTS_BY_USERID, postMapper,
            postedBy
        );
    }

    @Override
    public Post findPost(UUID postId) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_POST, postMapper, postId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int likePost(UUID postId, UUID likedBy) {
        try {
            jdbcTemplate.update(SQL_LIKE_POST, postId, likedBy);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int retweetPost(UUID postId, UUID retweetedBy) {
        try {
            jdbcTemplate.update(SQL_RETWEET_POST, postId, retweetedBy);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<UUID> findPostLikes(UUID postId) {
        return jdbcTemplate.query(SQL_LIKED_BY,
            (rs, rowNum) -> UUID.fromString(rs.getString("liked_by")),
            postId
        );
    }

    @Override
    public List<UUID> findPostRetweets(UUID postId) {
        return jdbcTemplate.query(SQL_RETWEETED_BY,
            (rs, rowNum) -> UUID.fromString(rs.getString("retweeted_by")),
            postId
        );
    }

    @Override
    public int deletePost(UUID postId) {
        try {
            jdbcTemplate.update(SQL_DELETE_POST, postId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int unlikePost(UUID postId, UUID likedBy) {
        try {
            jdbcTemplate.update(SQL_UNLIKE_POST, postId, likedBy);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int undoRetweetPost(UUID postId, UUID retweetedBy) {
        try {
            jdbcTemplate.update(SQL_UNDO_RETWEET_POST, postId, retweetedBy);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Post> findComments(UUID repliedTo) {
        return jdbcTemplate.query(SQL_FIND_COMMENTS, postMapper, repliedTo);
    }

    private final RowMapper<Post> postMapper =  (rs, rowNum) -> new Post(
            UUID.fromString(rs.getString("post_id")),
            UUID.fromString(rs.getString("posted_by")),
            rs.getString("message"),
            rs.getTimestamp("created_on"),
            (UUID) rs.getObject("replied_to") // this can be null
    );
}
