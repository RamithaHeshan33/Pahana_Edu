package org.example.pahana_edu.service;

import org.example.pahana_edu.business.book.dto.BookDTO;
import org.example.pahana_edu.business.book.service.BookService;
import org.example.pahana_edu.persistance.book.dao.BookDAO;
import org.example.pahana_edu.persistance.book.model.BookModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;
    private FakeBookDAO fakeBookDAO;
    private BookModel bookModel;
    private BookDTO expectedBookDTO;

    @BeforeEach
    void setUp() {
        fakeBookDAO = new FakeBookDAO();
        bookService = new BookService(fakeBookDAO);

        bookModel = BookModel.builder()
                .id(1)
                .title("Test Book")
                .author("Test Author")
                .publisher("Test Publisher")
                .isbn("1234567890")
                .quantity(10)
                .price(29.99)
                .description("Test Description")
                .category("1")
                .image("Uploads/test.jpg")
                .language("English")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        expectedBookDTO = BookDTO.builder()
                .id(1)
                .title("Test Book")
                .author("Test Author")
                .publisher("Test Publisher")
                .isbn("1234567890")
                .quantity(10)
                .price(29.99)
                .categoryId("1")
                .categoryName("1")
                .image("Uploads/test.jpg")
                .language("English")
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // Fake BookDAO implementation for testing //test
    private static class FakeBookDAO extends BookDAO {
        private List<BookModel> books = new ArrayList<>();
        private boolean shouldFail = false;

        void setShouldFail(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }

        void addBook(BookModel book) {
            books.add(book);
        }

        @Override
        public BookModel saveBook(BookModel book) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            book.setId(books.size() + 1);
            books.add(book);
            return book;
        }

        @Override
        public BookModel getBookById(int id) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return books.stream()
                    .filter(b -> b.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<BookModel> getAllBooks() throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return new ArrayList<>(books);
        }

        @Override
        public BookModel updateBook(BookModel book) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getId().equals(book.getId())) {
                    books.set(i, book);
                    return book;
                }
            }
            throw new SQLException("Book not found");
        }

        @Override
        public boolean deleteBook(int id) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return books.removeIf(b -> b.getId() == id);
        }

        @Override
        public boolean existsByIsbn(String isbn) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return books.stream().anyMatch(b -> b.getIsbn().equals(isbn));
        }

        @Override
        public boolean existsByIsbnExcludingId(String isbn, int excludeId) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return books.stream()
                    .anyMatch(b -> b.getIsbn().equals(isbn) && b.getId() != excludeId);
        }
    }

    @Test
    void getAllBooks() throws SQLException {
        fakeBookDAO.saveBook(bookModel);

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedBookDTO.getTitle(), result.get(0).getTitle());
        assertEquals(expectedBookDTO.getAuthor(), result.get(0).getAuthor());
        assertEquals(expectedBookDTO.getIsbn(), result.get(0).getIsbn());
    }

    @Test
    void getAllBooks_EmptyList() throws SQLException {
        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getBookById_Success() throws SQLException {
        fakeBookDAO.saveBook(bookModel);

        BookDTO result = bookService.getBookById(1);

        assertNotNull(result);
        assertEquals(expectedBookDTO.getTitle(), result.getTitle());
        assertEquals(expectedBookDTO.getAuthor(), result.getAuthor());
        assertEquals(expectedBookDTO.getIsbn(), result.getIsbn());
    }

    @Test
    void getBookById_NotFound() throws SQLException {
        BookDTO result = bookService.getBookById(999);

        assertNull(result);
    }

    @Test
    void saveBook_Success() throws SQLException {
        fakeBookDAO.saveBook(bookModel);

        BookModel savedBook = fakeBookDAO.getBookById(1);
        assertNotNull(savedBook);
        assertEquals(bookModel.getTitle(), savedBook.getTitle());
    }

    @Test
    void updateBook_Success() throws SQLException {
        fakeBookDAO.saveBook(bookModel);
        BookModel updatedModel = BookModel.builder()
                .id(1)
                .title("Updated Book")
                .author("Updated Author")
                .publisher("Updated Publisher")
                .isbn("1234567890")
                .quantity(15)
                .price(39.99)
                .description("Updated Description")
                .category("1")
                .image("Uploads/updated.jpg")
                .language("English")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        fakeBookDAO.updateBook(updatedModel);

        BookModel result = fakeBookDAO.getBookById(1);
        assertNotNull(result);
        assertEquals("Updated Book", result.getTitle());
        assertEquals(39.99, result.getPrice());
    }

    @Test
    void deleteBook_Success() throws SQLException {
        fakeBookDAO.saveBook(bookModel);

        boolean deleted = bookService.deleteBook(1, null);

        assertTrue(deleted);
        assertNull(fakeBookDAO.getBookById(1));
    }

    @Test
    void deleteBook_NotFound() throws SQLException {
        boolean deleted = bookService.deleteBook(999, null);

        assertFalse(deleted);
    }

    @Test
    void getAllBooks_DatabaseError() {
        fakeBookDAO.setShouldFail(true);

        SQLException exception = assertThrows(SQLException.class, () -> {
            bookService.getAllBooks();
        });
        assertEquals("Simulated database error", exception.getMessage());
    }

    @Test
    void getBookById_DatabaseError() {
        fakeBookDAO.setShouldFail(true);

        SQLException exception = assertThrows(SQLException.class, () -> {
            bookService.getBookById(1);
        });
        assertEquals("Simulated database error", exception.getMessage());
    }

    @Test
    void updateBook_NotFound() throws SQLException {
        BookModel nonExistentBook = BookModel.builder()
                .id(999)
                .title("Non-existent Book")
                .author("Unknown Author")
                .publisher("Unknown Publisher")
                .isbn("9999999999")
                .quantity(5)
                .price(29.99)
                .description("Non-existent Description")
                .category("1")
                .image("Uploads/nonexistent.jpg")
                .language("English")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SQLException exception = assertThrows(SQLException.class, () -> {
            fakeBookDAO.updateBook(nonExistentBook);
        });
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void existsByIsbn_Found() throws SQLException {
        fakeBookDAO.saveBook(bookModel);

        boolean exists = fakeBookDAO.existsByIsbn("1234567890");

        assertTrue(exists);
    }

    @Test
    void existsByIsbn_NotFound() throws SQLException {
        boolean exists = fakeBookDAO.existsByIsbn("9999999999"); // different isbn

        assertFalse(exists);
    }

    @Test
    void existsByIsbnExcludingId_Found() throws SQLException {
        fakeBookDAO.saveBook(bookModel);
        BookModel anotherBook = BookModel.builder()
                .id(2)
                .title("Another Book")
                .author("Another Author")
                .publisher("Another Publisher")
                .isbn("1234567890") // Same ISBN
                .quantity(8)
                .price(39.99)
                .description("Another Description")
                .category("1")
                .image("Uploads/another.jpg")
                .language("English")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        fakeBookDAO.saveBook(anotherBook);

        boolean exists = fakeBookDAO.existsByIsbnExcludingId("1234567890", 1);

        assertTrue(exists);
    }

    @Test
    void existsByIsbnExcludingId_NotFound() throws SQLException {
        fakeBookDAO.saveBook(bookModel);

        boolean exists = fakeBookDAO.existsByIsbnExcludingId("1234567890", 1);

        assertFalse(exists);
    }
}