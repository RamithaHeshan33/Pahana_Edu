package org.example.pahana_edu.persistance.category.dao;

import org.example.pahana_edu.persistance.category.model.CategoryModel;
import org.example.pahana_edu.util.DBConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static CategoryModel getCategoryById(int id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CategoryModel category = new CategoryModel(rs.getString("name"), rs.getString("description"));
                    category.setId(rs.getInt("id"));
                    return category;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting category: " + e.getMessage(), e);
        }
        return null;
    }

    public static List<CategoryModel> getAllCategories() throws SQLException {
        String sql = "SELECT * FROM categories ORDER BY name";
        List<CategoryModel> categories = new ArrayList<>();

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CategoryModel category = new CategoryModel(rs.getString("name"), rs.getString("description"));
                category.setId(rs.getInt("id"));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting all categories: " + e.getMessage(), e);
        }
        return categories;
    }

    public static CategoryModel updateCategory(CategoryModel category) throws SQLException {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getCategoryDescription());
            stmt.setInt(3, category.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating Category failed: No rows affected");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating category: " + e.getMessage(), e);
        }
        return category;
    }

    public static boolean deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new SQLException("Error deleting category: " + e.getMessage(), e);
        }
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

    public boolean existingCategoryExcludingId(String categoryName, int excludeId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE name = ? AND id != ?";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, categoryName);
            stmt.setInt(2, excludeId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
