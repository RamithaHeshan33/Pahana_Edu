package org.example.pahana_edu.business.cart.dto;

public class CartItemDTO {
    private Integer bookId;
    private String title;
    private Double price;
    private Integer quantity;
    private String image;

    // Default constructor
    public CartItemDTO() {}

    // Constructor with all fields
    public CartItemDTO(Integer bookId, String title, Double price, Integer quantity, String image) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    // Getters and Setters
    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Calculate total price for this item
    public Double getTotalPrice() {
        return price * quantity;
    }

    // Validation method
    public boolean isValid() {
        return bookId != null && bookId > 0 &&
                title != null && !title.trim().isEmpty() &&
                price != null && price >= 0 &&
                quantity != null && quantity > 0;
    }
}
