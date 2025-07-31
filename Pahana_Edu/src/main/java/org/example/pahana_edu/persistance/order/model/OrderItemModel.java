package org.example.pahana_edu.persistance.order.model;

public class OrderItemModel {
    private Integer id;
    private Integer orderId;
    private Integer bookId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String bookTitle;

    // Constructor for creating new order item with book title
    public OrderItemModel(Integer orderId, Integer bookId, String bookTitle, Integer quantity, Double unitPrice) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * quantity;
    }

    // Default constructor
    public OrderItemModel() {}

    // Constructor for creating new order item
    public OrderItemModel(Integer orderId, Integer bookId, Integer quantity, Double unitPrice) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * quantity;
    }

    // Constructor with all fields
    public OrderItemModel(Integer id, Integer orderId, Integer bookId, String bookTitle, Integer quantity,
                          Double unitPrice, Double totalPrice) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
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

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
