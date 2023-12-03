package database;

import oauth.User;

import java.sql.SQLException;
import java.util.Collection;

public interface OauthDAO {
    User getUserById(Long result_id) throws SQLException;
    String authenticateUser(String username, String password) throws SQLException;
    User registerUser(String username, String password, String name) throws SQLException;
    User findUserByUsername(String username) throws SQLException;
}