package org.example.pahana_edu.business.book.dto;

import java.time.LocalDateTime;

public class BookDTO {
    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Integer quantity;
    private Double price;
    private String categoryId;
    private String categoryName;
    private String image;
    private String language;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public BookDTO() {}

    // Constructor for creating new book
    public BookDTO(String title, String author, String publisher, String isbn,
                   Integer quantity, Double price, String categoryId, String image,
                   String language, String description) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = categoryId;
        this.image = image;
        this.language = language;
        this.description = description;
    }

    // Constructor with all fields
    public BookDTO(Integer id, String title, String author, String publisher, String isbn,
                   Integer quantity, Double price, String categoryId, String categoryName,
                   String image, String language, String description,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.quantity = quantity;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.image = image;
        this.language = language;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Validation method
    public boolean isValid() {
        return title != null && !title.trim().isEmpty() &&
                quantity != null && quantity >= 0 &&
                price != null && price >= 0 &&
                categoryId != null && !categoryId.trim().isEmpty();
    }
}
