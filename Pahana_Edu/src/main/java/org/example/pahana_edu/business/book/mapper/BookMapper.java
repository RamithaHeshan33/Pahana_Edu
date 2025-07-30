package org.example.pahana_edu.business.book.mapper;

import org.example.pahana_edu.business.book.dto.BookDTO;
import org.example.pahana_edu.persistance.book.model.BookModel;

public class BookMapper {

    public static BookModel toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }

        return new BookModel(
                bookDTO.getPrice(),
                bookDTO.getId(),
                bookDTO.getTitle(),
                bookDTO.getAuthor(),
                bookDTO.getPublisher(),
                bookDTO.getIsbn(),
                bookDTO.getQuantity(),
                bookDTO.getDescription(),
                bookDTO.getCategoryId(),
                bookDTO.getImage(),
                bookDTO.getLanguage(),
                bookDTO.getCreatedAt(),
                bookDTO.getUpdatedAt()
        );
    }

    public static BookDTO toDTO(BookModel bookModel) {
        if (bookModel == null) {
            return null;
        }

        return new BookDTO(
                bookModel.getId(),
                bookModel.getTitle(),
                bookModel.getAuthor(),
                bookModel.getPublisher(),
                bookModel.getIsbn(),
                bookModel.getQuantity(),
                bookModel.getPrice(),
                null,
                bookModel.getCategory(),
                bookModel.getImage(),
                bookModel.getLanguage(),
                bookModel.getDescription(),
                bookModel.getCreatedAt(),
                bookModel.getUpdatedAt()
        );
    }
}
