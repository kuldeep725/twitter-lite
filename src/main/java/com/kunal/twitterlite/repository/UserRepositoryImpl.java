package com.kunal.twitterlite.repository;


import com.kunal.twitterlite.exception.TwitterAuthException;
import com.kunal.twitterlite.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE_USER = "INSERT INTO twitter_user(username, password) VALUES (?, ?)";
    private static final String SQL_FIND_ALL_USERS = "SELECT user_id, username, password FROM twitter_user";
    private static final String SQL_FIND_USER_BY_ID = "SELECT user_id, username, password FROM twitter_user WHERE user_id=?";
    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(*) FROM twitter_user WHERE username=?";
    private static final String SQL_FIND_USER_BY_USERNAME = "SELECT user_id, username, password FROM twitter_user WHERE username=?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public UUID create(String username, String password) throws TwitterAuthException {
        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, username);
                ps.setString(2, hashedPassword);
                return ps;
            }, keyHolder);

            return (UUID) Objects.requireNonNull(keyHolder.getKeys()).get("user_id");
        } catch (Exception e) {
            throw new TwitterAuthException("Invalid details. Failed to create account | " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public int getCountByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject(SQL_COUNT_BY_USERNAME, Integer.class, username);
        return (count != null ? count : 0);
    }

    @Override
    public User findById(UUID userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_ID, userRowMapper, userId);
    }

    @Override
    public User findByUsername(String username) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_USERNAME, userRowMapper, username);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) throws TwitterAuthException{
        try {
            User user = findByUsername(username);
            if(!BCrypt.checkpw(password, user.getPassword()))
                throw new TwitterAuthException("Invalid email/password");

            return user;
        } catch (Exception e) {
            throw new TwitterAuthException("Invalid email/password | " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(SQL_FIND_ALL_USERS, userRowMapper);
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum) -> new User(
            UUID.fromString(rs.getString("user_id")),
            rs.getString("username"),
            rs.getString("password")
        ));
}
