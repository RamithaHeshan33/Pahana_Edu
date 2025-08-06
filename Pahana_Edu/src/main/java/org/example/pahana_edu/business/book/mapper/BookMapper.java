package org.example.pahana_edu.business.book.mapper;

import org.example.pahana_edu.business.book.dto.BookDTO;
import org.example.pahana_edu.persistance.book.model.BookModel;

public class BookMapper {

    public static BookModel toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }

        return BookModel.builder()
                .id(bookDTO.getId())
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .publisher(bookDTO.getPublisher())
                .isbn(bookDTO.getIsbn())
                .quantity(bookDTO.getQuantity())
                .price(bookDTO.getPrice())
                .description(bookDTO.getDescription())
                .category(bookDTO.getCategoryId())
                .image(bookDTO.getImage())
                .language(bookDTO.getLanguage())
                .createdAt(bookDTO.getCreatedAt())
                .updatedAt(bookDTO.getUpdatedAt())
                .build();
    }

    public static BookDTO toDTO(BookModel bookModel) {
        if (bookModel == null) {
            return null;
        }

        return BookDTO.builder()
                .id(bookModel.getId())
                .title(bookModel.getTitle())
                .author(bookModel.getAuthor())
                .publisher(bookModel.getPublisher())
                .isbn(bookModel.getIsbn())
                .quantity(bookModel.getQuantity())
                .price(bookModel.getPrice())
                .categoryId(null)
                .categoryName(bookModel.getCategory())
                .image(bookModel.getImage())
                .language(bookModel.getLanguage())
                .description(bookModel.getDescription())
                .createdAt(bookModel.getCreatedAt())
                .updatedAt(bookModel.getUpdatedAt())
                .build();
    }
}