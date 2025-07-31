package org.example.pahana_edu.business.order.dto;

public class OrderItemDTO {
    private Integer id;
    private Integer orderId;
    private Integer bookId;
    private String bookTitle;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

    // Default constructor
    public OrderItemDTO() {}

    // Constructor for creating new order item
    public OrderItemDTO(Integer orderId, Integer bookId, String bookTitle, Integer quantity, Double unitPrice) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * quantity;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Validation method
    public boolean isValid() {
        return orderId != null && orderId > 0 &&
                bookId != null && bookId > 0 &&
                quantity != null && quantity > 0 &&
                unitPrice != null && unitPrice >= 0;
    }
}
