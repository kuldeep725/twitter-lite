package com.kunal.twitterlite.repository;


import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_FIND_ALL_USERS = "SELECT user_id, username, password FROM twitter_user";
    private static final String SQL_FIND_USER_BY_ID = "SELECT user_id, username, password FROM twitter_user WHERE user_id=?";
    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(*) FROM twitter_user WHERE username=?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int create(User user) throws TwitterAuthException {
        return 0;
    }

    @Override
    public int getCountByUsername(String username) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_USERNAME, Integer.class, new Object[] {username});
    }

    @Override
    public User findById(UUID userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_ID, userRowMapper, new Object[] {userId});
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(SQL_FIND_ALL_USERS, userRowMapper);
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User((UUID) UUID.fromString(rs.getString("user_id")),
                rs.getString("username"),
                rs.getString("password"));
    });
}
