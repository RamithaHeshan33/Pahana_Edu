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

        bookModel = new BookModel(
                29.99,
                1,
                "Test Book",
                "Test Author",
                "Test Publisher",
                "1234567890",
                10,
                "Test Description",
                "1",
                "Uploads/test.jpg",
                "English",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        expectedBookDTO = new BookDTO(
                1,
                "Test Book",
                "Test Author",
                "Test Publisher",
                "1234567890",
                10,
                29.99,
                "1",
                "1",
                "Uploads/test.jpg",
                "English",
                "Test Description",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // Fake BookDAO implementation for testing
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
        BookModel updatedModel = new BookModel(
                39.99,
                1,
                "Updated Book",
                "Updated Author",
                "Updated Publisher",
                "1234567890",
                15,
                "Updated Description",
                "1",
                "Uploads/updated.jpg",
                "English",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

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
        BookModel nonExistentBook = new BookModel(
                29.99,
                999,
                "Non-existent Book",
                "Unknown Author",
                "Unknown Publisher",
                "9999999999",
                5,
                "Non-existent Description",
                "1",
                "Uploads/nonexistent.jpg",
                "English",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

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
        BookModel anotherBook = new BookModel(
                39.99,
                2,
                "Another Book",
                "Another Author",
                "Another Publisher",
                "1234567890", // Same ISBN
                8,
                "Another Description",
                "1",
                "Uploads/another.jpg",
                "English",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
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