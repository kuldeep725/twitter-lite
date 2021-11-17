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
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE_USER = "INSERT INTO twitter_user(username, password) VALUES (?, ?)";
    private static final String SQL_FIND_ALL_USERS = "SELECT user_id, username, password FROM twitter_user";
    private static final String SQL_FIND_USER_BY_ID = "SELECT user_id, username, password FROM twitter_user WHERE user_id=?";
    private static final String SQL_COUNT_BY_USERNAME = "SELECT COUNT(*) FROM twitter_user WHERE username=?";
    private static final String SQL_FIND_USER_BY_USRENAME = "SELECT user_id, username, password FROM twitter_user WHERE username=?";

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

            return (UUID) keyHolder.getKeys().get("user_id");
        } catch (Exception e) {
            throw new TwitterAuthException("Invalid details. Failed to create account | " + e.getStackTrace());
        }
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
    public User findByUsername(String username) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_USRENAME, userRowMapper, new Object[] {username});
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) throws TwitterAuthException{
        try {
            User user = findByUsername(username);
            if(!BCrypt.checkpw(password, user.getPassword()))
                throw new TwitterAuthException("Invalid email/password");

            return user;
        } catch (Exception e) {
            throw new TwitterAuthException("Invalid email/password | " + e.getStackTrace());
        }
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
