package org.example.pahana_edu.business.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Integer id;
    private Integer customerId;
    private String customerName;
    private String customerPhone;
    private Double totalAmount;
    private Double cashReceived;
    private Double changeAmount;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemDTO> orderItems;

    // Default constructor
    public OrderDTO() {}

    // Constructor for creating new order
    public OrderDTO(Integer customerId, Double totalAmount, Double cashReceived, Double changeAmount) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.cashReceived = cashReceived;
        this.changeAmount = changeAmount;
        this.status = "completed";
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

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    // Validation method
    public boolean isValid() {
        return customerId != null && customerId > 0 &&
                totalAmount != null && totalAmount >= 0 &&
                cashReceived != null && cashReceived >= 0 &&
                changeAmount != null;
    }
}
