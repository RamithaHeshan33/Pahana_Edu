package org.example.pahana_edu.persistance.book.dao;

import org.example.pahana_edu.persistance.book.model.BookModel;
import org.example.pahana_edu.util.DBConn;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public BookModel saveBook(BookModel book) throws SQLException {
        String sql = "INSERT INTO books (title, author, publisher, isbn_number, quantity, price, category_id, image_path, language, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getIsbn());
            stmt.setInt(5, book.getQuantity());
            stmt.setDouble(6, book.getPrice());
            stmt.setInt(7, Integer.parseInt(book.getCategory()));
            stmt.setString(8, book.getImage());
            stmt.setString(9, book.getLanguage());
            stmt.setString(10, book.getDescription());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating book failed: No rows affected");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    book.setId(keys.getInt(1));
                } else {
                    throw new SQLException("Creating book failed: No ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error saving book: " + e.getMessage(), e);
        }
        return book;
    }

    public BookModel getBookById(int id) throws SQLException {
        String sql = "SELECT b.*, c.name as category_name FROM books b LEFT JOIN categories c ON b.category_id = c.id WHERE b.id = ?";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting book: " + e.getMessage(), e);
        }
        return null;
    }

    public List<BookModel> getAllBooks() throws SQLException {
        String sql = "SELECT b.*, c.name as category_name FROM books b LEFT JOIN categories c ON b.category_id = c.id ORDER BY b.created_at DESC";
        List<BookModel> books = new ArrayList<>();

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting all books: " + e.getMessage(), e);
        }
        return books;
    }

    public BookModel updateBook(BookModel book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, isbn_number = ?, quantity = ?, price = ?, category_id = ?, image_path = ?, language = ?, description = ? WHERE id = ?";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getIsbn());
            stmt.setInt(5, book.getQuantity());
            stmt.setDouble(6, book.getPrice());
            stmt.setInt(7, Integer.parseInt(book.getCategory())); // category_id
            stmt.setString(8, book.getImage());
            stmt.setString(9, book.getLanguage());
            stmt.setString(10, book.getDescription());
            stmt.setInt(11, book.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating book failed: No rows affected");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating book: " + e.getMessage(), e);
        }
        return book;
    }

    public boolean deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new SQLException("Error deleting book: " + e.getMessage(), e);
        }
    }

    public boolean existsByIsbn(String isbn) throws SQLException {
        if (isbn == null || isbn.trim().isEmpty()) {
            // If ISBN is null or blank, skip the check
            return false;
        }

        String sql = "SELECT COUNT(*) FROM books WHERE isbn_number = ?";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    public boolean existsByIsbnExcludingId(String isbn, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn_number = ? AND id != ?";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            stmt.setInt(2, excludeId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private BookModel mapResultSetToBook(ResultSet rs) throws SQLException {
        BookModel book = new BookModel(
                rs.getDouble("price"),
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("publisher"),
                rs.getString("isbn_number"),
                rs.getInt("quantity"),
                rs.getString("description"),
                rs.getString("category_name"), // Use category name instead of ID for display
                rs.getString("image_path"),
                rs.getString("language"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : LocalDateTime.now(),
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : LocalDateTime.now()
        );
        return book;
    }
}
