package org.example.pahana_edu.persistance.order.dao;

import org.example.pahana_edu.persistance.order.model.OrderItemModel;
import org.example.pahana_edu.persistance.order.model.OrderModel;
import org.example.pahana_edu.util.DBConn;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public OrderModel saveOrder(OrderModel order) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, total_amount, cash_received, change_amount, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getCustomerId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setDouble(3, order.getCashReceived());
            stmt.setDouble(4, order.getChangeAmount());
            stmt.setString(5, order.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed: No rows affected");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setId(keys.getInt(1));
                } else {
                    throw new SQLException("Creating order failed: No ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error saving order: " + e.getMessage(), e);
        }
        return order;
    }

    public OrderItemModel saveOrderItem(OrderItemModel orderItem) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, book_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getBookId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setDouble(4, orderItem.getUnitPrice());
            stmt.setDouble(5, orderItem.getTotalPrice());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order item failed: No rows affected");
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    orderItem.setId(keys.getInt(1));
                } else {
                    throw new SQLException("Creating order item failed: No ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error saving order item: " + e.getMessage(), e);
        }
        return orderItem;
    }

    public boolean updateBookQuantity(int bookId, int quantitySold) throws SQLException {
        String sql = "UPDATE books SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, quantitySold);
            stmt.setInt(2, bookId);
            stmt.setInt(3, quantitySold);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new SQLException("Error updating book quantity: " + e.getMessage(), e);
        }
    }

    public OrderModel getOrderById(int id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting order: " + e.getMessage(), e);
        }
        return null;
    }

    public List<OrderModel> getAllOrders() throws SQLException {
        String sql = "SELECT o.*, c.name as customer_name, c.phone as customer_phone FROM orders o " +
                "LEFT JOIN customers c ON o.customer_id = c.id ORDER BY o.order_date DESC";
        List<OrderModel> orders = new ArrayList<>();

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                OrderModel order = mapResultSetToOrder(rs);
                // Set customer information
                order.setCustomerName(rs.getString("customer_name"));
                order.setCustomerPhone(rs.getString("customer_phone"));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting all orders: " + e.getMessage(), e);
        }
        return orders;
    }

    public List<OrderItemModel> getOrderItemsByOrderId(int orderId) throws SQLException {
        String sql = "SELECT oi.*, b.title as book_title FROM order_items oi " +
                "LEFT JOIN books b ON oi.book_id = b.id WHERE oi.order_id = ?";
        List<OrderItemModel> orderItems = new ArrayList<>();

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItemModel orderItem = mapResultSetToOrderItem(rs);
                    // Set book title
                    orderItem.setBookTitle(rs.getString("book_title"));
                    orderItems.add(orderItem);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting order items: " + e.getMessage(), e);
        }
        return orderItems;
    }

    private OrderModel mapResultSetToOrder(ResultSet rs) throws SQLException {
        OrderModel order = new OrderModel(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getDouble("total_amount"),
                rs.getDouble("cash_received"),
                rs.getDouble("change_amount"),
                rs.getTimestamp("order_date") != null ? rs.getTimestamp("order_date").toLocalDateTime() : LocalDateTime.now(),
                rs.getString("status")
        );
        return order;
    }

    private OrderItemModel mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        return new OrderItemModel(
                rs.getInt("id"),
                rs.getInt("order_id"),
                rs.getInt("book_id"),
                rs.getString("book_title"), // Add book title from the joined query
                rs.getInt("quantity"),
                rs.getDouble("unit_price"),
                rs.getDouble("total_price")
        );
    }
}