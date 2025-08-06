package org.example.pahana_edu.persistance.book.model;

import java.time.LocalDateTime;

public class BookModel {
    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Integer quantity;
    private Double price;
    private String description;
    private String category;
    private String image;
    private String language;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor for builder
    private BookModel(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.author = builder.author;
        this.publisher = builder.publisher;
        this.isbn = builder.isbn;
        this.quantity = builder.quantity;
        this.price = builder.price;
        this.description = builder.description;
        this.category = builder.category;
        this.image = builder.image;
        this.language = builder.language;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // Default constructor for frameworks
    public BookModel() {}

    // Legacy constructor for backward compatibility
    public BookModel(Double price, Integer id, String title, String author, String publisher, String isbn, Integer quantity,
                     String description, String category, String image, String language, LocalDateTime createdAt,
                     LocalDateTime updatedAt) {
        this.price = price;
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
        this.image = image;
        this.language = language;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Static method to create builder
    public static Builder builder() {
        return new Builder();
    }

    // Builder class
    public static class Builder {
        private Integer id;
        private String title;
        private String author;
        private String publisher;
        private String isbn;
        private Integer quantity;
        private Double price;
        private String description;
        private String category;
        private String image;
        private String language;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BookModel build() {
            return new BookModel(this);
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}