package org.example.pahana_edu.persistance.customer.dao;

import org.example.pahana_edu.persistance.customer.model.CustomerModel;
import org.example.pahana_edu.util.DBConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public static CustomerModel saveCustomer(CustomerModel customer) throws SQLException {
        String sql = "INSERT INTO customers (name, email, phone, address, account_number) VALUES (?,?,?,?,?)";
        try {
            Connection connection = DBConn.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getCustomerEmail());
            stmt.setString(3, customer.getCustomerPhone());
            stmt.setString(4, customer.getCustomerAddress());
            stmt.setString(5, customer.getCustomerAccountNumber());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Adding customer failed");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setCustomerId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating customer failed");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error adding customer: " + ex.getMessage());
        }
        return customer;
    }

    public List<CustomerModel> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers";
        List<CustomerModel> customers = new ArrayList<>();

        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                CustomerModel customer = new CustomerModel(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("account_number")
                );
                customer.setCustomerId(rs.getInt("id"));
                customers.add(customer);
            }
        } catch (SQLException ex) {
            throw new SQLException("Error fetching customers: " + ex.getMessage(), ex);
        }
        return customers;
    }

    public CustomerModel getCustomerById(int id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CustomerModel customer = new CustomerModel(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("account_number")
                    );
                    customer.setCustomerId(rs.getInt("id"));
                    return customer;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error getting customer: " + e.getMessage(), e);
        }
        return null;
    }

    public CustomerModel updateCustomer(CustomerModel customer) throws SQLException {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ?, address = ?, account_number = ? WHERE id = ?";
        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getCustomerEmail());
            stmt.setString(3, customer.getCustomerPhone());
            stmt.setString(4, customer.getCustomerAddress());
            stmt.setString(5, customer.getCustomerAccountNumber());
            stmt.setInt(6, customer.getCustomerId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating customer failed: No rows affected");
            }
        } catch (SQLException e) {
            throw new SQLException("Error updating customer: " + e.getMessage(), e);
        }
        return customer;
    }

    public boolean deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (Connection connection = DBConn.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new SQLException("Error deleting customer: " + e.getMessage(), e);
        }
    }

    public CustomerModel findByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM customers WHERE phone = ?";

        try (Connection connection = DBConn.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) { stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CustomerModel customer = new CustomerModel(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("account_number")
                    );
                    customer.setCustomerId(rs.getInt("id"));
                    return customer;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error finding customer by phone: " + e.getMessage(), e);
        }
        return null;
    }
}