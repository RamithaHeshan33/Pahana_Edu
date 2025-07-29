package org.example.pahana_edu.dao;

import org.example.pahana_edu.model.CategoryModel;
import org.example.pahana_edu.util.DBConn;

import java.sql.*;

public class CategoryDAO {
    public static CategoryModel saveCategory(CategoryModel category) throws SQLException {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getCategoryDescription());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating Category failed: No rows affected");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    category.setId(keys.getInt(1));
                } else {
                    throw new SQLException("Creating Category failed: No ID obtained");
                }
            }
        } catch (SQLException e) {
            // Log the error (e.g., using a logging framework like SLF4J)
            throw new SQLException("Error saving category: " + e.getMessage(), e);
        }
        return category;
    }

    public static CategoryModel getCategory(CategoryModel category) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, category.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Getting Category failed: No rows affected");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    category.setId(keys.getInt(1));
                } else {
                    throw new SQLException("Getting Category failed: No ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting category: " + e.getMessage(), e);
        }
        return category;
    }

    public boolean existingCategory(String categoryName) throws SQLException {
        String sql = "SELECT * FROM categories WHERE name = ?";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, categoryName);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
