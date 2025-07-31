package org.example.pahana_edu.persistance.order.model;

import java.time.LocalDateTime;

public class OrderModel {
    private Integer id;
    private Integer customerId;
    private Double totalAmount;
    private Double cashReceived;
    private Double changeAmount;
    private LocalDateTime orderDate;
    private String status;
    private String customerName;
    private String customerPhone;

    // Default constructor
    public OrderModel() {}

    // Constructor for creating new order
    public OrderModel(Integer customerId, Double totalAmount, Double cashReceived, Double changeAmount) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.cashReceived = cashReceived;
        this.changeAmount = changeAmount;
        this.status = "completed";
        this.orderDate = LocalDateTime.now();
    }

    // Constructor with all fields
    public OrderModel(Integer id, Integer customerId, Double totalAmount, Double cashReceived,
                      Double changeAmount, LocalDateTime orderDate, String status) {
        this.id = id;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.cashReceived = cashReceived;
        this.changeAmount = changeAmount;
        this.orderDate = orderDate;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getCashReceived() {
        return cashReceived;
    }

    public void setCashReceived(Double cashReceived) {
        this.cashReceived = cashReceived;
    }

    public Double getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Double changeAmount) {
        this.changeAmount = changeAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
