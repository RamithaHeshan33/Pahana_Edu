package org.example.pahana_edu.dao;

import org.example.pahana_edu.model.UserModel;
import org.example.pahana_edu.util.DBConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    public UserModel save(UserModel user) throws SQLException {
        String sql = "INSERT INTO admins (username, email, password, first_name, last_name) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId((int) generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }

        return user;
    }

    public Optional<UserModel> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM admins WHERE username = ?";

        try (Connection conn = DBConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }

        return Optional.empty();
    }

    public Optional<UserModel> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM admins WHERE email = ?";

        try (Connection conn = DBConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }

        return Optional.empty();
    }

    public Optional<UserModel> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM admins WHERE id = ?";

        try (Connection conn = DBConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
        }

        return Optional.empty();
    }

    public List<UserModel> findAll() throws SQLException {
        String sql = "SELECT * FROM admins";
        List<UserModel> users = new ArrayList<>();

        try (Connection conn = DBConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }

        return users;
    }

    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admins WHERE username = ?";

        try (Connection conn = DBConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admins WHERE email = ?";

        try (Connection conn = DBConn.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    private UserModel mapResultSetToUser(ResultSet rs) throws SQLException {
        UserModel user = new UserModel();
        user.setId((int) rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));

        return user;
    }
}
