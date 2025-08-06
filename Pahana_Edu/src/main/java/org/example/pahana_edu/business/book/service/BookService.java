package org.example.pahana_edu.business.book.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.pahana_edu.business.book.dto.BookDTO;
import org.example.pahana_edu.business.book.mapper.BookMapper;
import org.example.pahana_edu.persistance.book.dao.BookDAO;
import org.example.pahana_edu.persistance.book.model.BookModel;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BookService {
    private static final Logger LOGGER = Logger.getLogger(BookService.class.getName());
    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public List<BookDTO> getAllBooks() throws SQLException {
        List<BookModel> books = bookDAO.getAllBooks();
        return books.stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(int id) throws SQLException {
        BookModel book = bookDAO.getBookById(id);
        return book != null ? BookMapper.toDTO(book) : null;
    }

    public BookDTO saveBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Extract parameters from multipart request
            String title = getPartAsString(request.getPart("title"));
            String author = getPartAsString(request.getPart("author"));
            String publisher = getPartAsString(request.getPart("publisher"));
            String isbn = getPartAsString(request.getPart("isbn_number"));
            String quantityStr = getPartAsString(request.getPart("quantity"));
            String priceStr = getPartAsString(request.getPart("price"));
            String categoryId = getPartAsString(request.getPart("category"));
            String language = getPartAsString(request.getPart("language"));
            String description = getPartAsString(request.getPart("description"));

            // file uploading
            Part imagePart = request.getPart("image");
            String image = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                String fileName = imagePart.getSubmittedFileName();
                String uploadPath = request.getServletContext().getRealPath("") + File.separator + "Uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    boolean created = uploadDir.mkdirs();
                    if (!created) {
                        request.setAttribute("error", "Failed to create upload directory");
                        request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                        return null;
                    }
                }
                String filePath = uploadPath + File.separator + fileName;
                try {
                    imagePart.write(filePath);
                    image = "Uploads/" + fileName;
                } catch (IOException e) {
                    request.setAttribute("error", "Failed to upload image");
                    request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                    return null;
                }
            }

            // validation
            if (title == null || title.trim().isEmpty()) {
                request.setAttribute("error", "Title is required");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                request.setAttribute("error", "Quantity is required");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            if (priceStr == null || priceStr.trim().isEmpty()) {
                request.setAttribute("error", "Price is required");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            if (categoryId == null || categoryId.trim().isEmpty()) {
                request.setAttribute("error", "Category is required");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            Integer quantity;
            Double price;
            try {
                quantity = Integer.parseInt(quantityStr);
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid quantity or price format");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            // Set ISBN to null if empty
            if (isbn != null && isbn.trim().isEmpty()) {
                isbn = null;
            }

            if (publisher == null || publisher.trim().isEmpty()) {
                publisher = null;
            }

            if (author == null || author.trim().isEmpty()) {
                author = null;
            }

            // Create DTO
            BookDTO bookDTO = BookDTO.builder()
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .isbn(isbn)
                    .quantity(quantity)
                    .price(price)
                    .categoryId(categoryId)
                    .image(image)
                    .language(language)
                    .description(description)
                    .build();

            // Validate DTO
            if (!bookDTO.isValid()) {
                request.setAttribute("error", "Please fill in all required fields");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            // Check ISBN is already exist
            if (isbn != null) {
                if (bookDAO.existsByIsbn(isbn)) {
                    request.setAttribute("error", "A book with ISBN '" + isbn + "' already exists");
                    request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                    return null;
                }
            }

            // Convert DTO to Entity and save
            BookModel book = BookMapper.toEntity(bookDTO);
            BookModel savedBook = bookDAO.saveBook(book);

            // Set success message
            request.getSession().setAttribute("success", "Book '" + savedBook.getTitle() + "' has been added successfully!");

            return BookMapper.toDTO(savedBook);

        } catch (SQLException e) {
            LOGGER.severe("Database error: " + e.getMessage());
            request.setAttribute("error", "Database error occurred while saving book: " + e.getMessage());
            request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
            return null;
        } catch (Exception e) {
            LOGGER.severe("Error processing form data: " + e.getMessage());
            request.setAttribute("error", "Error processing form data: " + e.getMessage());
            request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
            return null;
        }
    }

    public BookDTO updateBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Extract parameters from multipart request
            String bookIdStr = getPartAsString(request.getPart("bookId"));
            String title = getPartAsString(request.getPart("title"));
            String author = getPartAsString(request.getPart("author"));
            String publisher = getPartAsString(request.getPart("publisher"));
            String isbn = getPartAsString(request.getPart("isbn_number"));
            String quantityStr = getPartAsString(request.getPart("quantity"));
            String priceStr = getPartAsString(request.getPart("price"));
            String categoryId = getPartAsString(request.getPart("category"));
            String language = getPartAsString(request.getPart("language"));
            String description = getPartAsString(request.getPart("description"));

            // Handle file upload
            Part imagePart = request.getPart("image");
            String image = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                String fileName = imagePart.getSubmittedFileName();
                String uploadPath = request.getServletContext().getRealPath("") + File.separator + "Uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    boolean created = uploadDir.mkdirs();
                    if (created) {
                        LOGGER.info("Created Uploads directory at: " + uploadPath);
                    } else {
                        LOGGER.severe("Failed to create Uploads directory at: " + uploadPath);
                        request.setAttribute("error", "Failed to create upload directory");
                        request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                        return null;
                    }
                }
                String filePath = uploadPath + File.separator + fileName;
                try {
                    imagePart.write(filePath);
                    image = "Uploads/" + fileName;
                    LOGGER.info("Image uploaded successfully to: " + filePath);
                } catch (IOException e) {
                    LOGGER.severe("Failed to write image file to: " + filePath + ". Error: " + e.getMessage());
                    request.setAttribute("error", "Failed to upload image");
                    request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                    return null;
                }
            } else {
                // Preserve existing image if no new file is uploaded
                BookDTO existingBook = getBookById(Integer.parseInt(bookIdStr));
                if (existingBook != null) {
                    image = existingBook.getImage();
                }
            }

            // Debug logging
            LOGGER.info("Book ID: " + bookIdStr);
            LOGGER.info("Title: " + title);
            LOGGER.info("Author: " + author);
            LOGGER.info("Publisher: " + publisher);
            LOGGER.info("ISBN: " + isbn);
            LOGGER.info("Quantity: " + quantityStr);
            LOGGER.info("Price: " + priceStr);
            LOGGER.info("Category: " + categoryId);
            LOGGER.info("Language: " + language);
            LOGGER.info("Description: " + description);
            LOGGER.info("Image: " + image);

            if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Book ID is required");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            int bookId;
            try {
                bookId = Integer.parseInt(bookIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid book ID format");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            // Validate required fields
            if (title == null || title.trim().isEmpty()) {
                request.setAttribute("error", "Title is required");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                request.setAttribute("error", "Quantity is required");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            if (priceStr == null || priceStr.trim().isEmpty()) {
                request.setAttribute("error", "Price is required");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            if (categoryId == null || categoryId.trim().isEmpty()) {
                request.setAttribute("error", "Category is required");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            Integer quantity;
            Double price;
            try {
                quantity = Integer.parseInt(quantityStr);
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid quantity or price format");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            // Set ISBN to null if empty
            if (isbn != null && isbn.trim().isEmpty()) {
                isbn = null;
            }

            // Create DTO
            BookDTO bookDTO = BookDTO.builder()
                    .id(bookId)
                    .title(title)
                    .author(author)
                    .publisher(publisher)
                    .isbn(isbn)
                    .quantity(quantity)
                    .price(price)
                    .categoryId(categoryId)
                    .categoryName(null)
                    .image(image)
                    .language(language)
                    .description(description)
                    .createdAt(null)
                    .updatedAt(null)
                    .build();

            // Validate DTO
            if (!bookDTO.isValid()) {
                request.setAttribute("error", "Please fill in all required fields");
                request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                return null;
            }

            // Check ISBN is already exist
            if (isbn != null) {
                if (bookDAO.existsByIsbnExcludingId(isbn, bookId)) {
                    request.setAttribute("error", "A book with ISBN '" + isbn + "' already exists");
                    request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
                    return null;
                }
            }

            // Convert DTO to Model
            BookModel book = BookMapper.toEntity(bookDTO);
            book.setId(bookId);

            // Update in database
            BookModel updatedBook = bookDAO.updateBook(book);

            // Set success message
            request.getSession().setAttribute("success", "Book '" + updatedBook.getTitle() + "' has been updated successfully!");

            return BookMapper.toDTO(updatedBook);
        } catch (SQLException e) {
            LOGGER.severe("Database error: " + e.getMessage());
            request.setAttribute("error", "Database error occurred while updating book: " + e.getMessage());
            request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
            return null;
        } catch (Exception e) {
            LOGGER.severe("Error processing form data: " + e.getMessage());
            request.setAttribute("error", "Error processing form data: " + e.getMessage());
            request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
            return null;
        }
    }

    private String getPartAsString(Part part) throws IOException {
        if (part == null) {
            return null;
        }
        byte[] bytes = new byte[(int) part.getSize()];
        part.getInputStream().read(bytes);
        return new String(bytes, "UTF-8").trim();
    }

    public boolean deleteBook(int id, HttpServletRequest request) throws SQLException {
        boolean deleted = bookDAO.deleteBook(id);
        if (deleted && request != null) {
            request.getSession().setAttribute("success", "Book has been deleted successfully!");
        }
        return deleted;
    }
}