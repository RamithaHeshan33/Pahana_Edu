package org.example.pahana_edu.business.book.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.pahana_edu.business.book.dto.BookDTO;
import org.example.pahana_edu.business.book.service.BookService;
import org.example.pahana_edu.business.category.dto.CategoryDTO;
import org.example.pahana_edu.business.category.service.CategoryService;
import org.example.pahana_edu.persistance.category.dao.CategoryDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "bookController", urlPatterns = {"/books/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class BookController extends HttpServlet {
    private final BookService bookService;
    private final CategoryService categoryService;

    public BookController() {
        this.bookService = new BookService();
        this.categoryService = new CategoryService(new CategoryDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        switch (pathInfo) {
            case "/ManageBooks":
                handleManageBooks(request, response);
                break;
            case "/getBook":
                handleGetBook(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (pathInfo) {
            case "/saveBook":
                handleSaveBook(request, response);
                break;
            case "/updateBook":
                handleUpdateBook(request, response);
                break;
            case "/deleteBook":
                handleDeleteBook(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleManageBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get all books
            List<BookDTO> books = bookService.getAllBooks();
            request.setAttribute("books", books);

            // Get all categories for dropdown
            List<CategoryDTO> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);

            request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error loading books: " + e.getMessage());
            request.getRequestDispatcher("/Admin/ManageBooks.jsp").forward(request, response);
        }
    }

    private void handleGetBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookIdStr = request.getParameter("id");
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdStr);
            BookDTO book = bookService.getBookById(bookId);

            if (book == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }

            // Return JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.format(
                    "{\"id\":%d,\"title\":\"%s\",\"author\":\"%s\",\"publisher\":\"%s\",\"isbn\":\"%s\",\"quantity\":%d,\"price\":%.2f,\"categoryName\":\"%s\",\"image\":\"%s\",\"language\":\"%s\",\"description\":\"%s\"}",
                    book.getId(),
                    escapeJson(book.getTitle()),
                    escapeJson(book.getAuthor()),
                    escapeJson(book.getPublisher() != null ? book.getPublisher() : ""),
                    escapeJson(book.getIsbn()),
                    book.getQuantity(),
                    book.getPrice(),
                    escapeJson(book.getCategoryName() != null ? book.getCategoryName() : ""),
                    escapeJson(book.getImage() != null ? book.getImage() : ""),
                    escapeJson(book.getLanguage()),
                    escapeJson(book.getDescription() != null ? book.getDescription() : "")
            ));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void handleSaveBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BookDTO savedBook = bookService.saveBook(request, response);
        if (savedBook != null) {
            response.sendRedirect(request.getContextPath() + "/books/ManageBooks");
        }
    }

    private void handleUpdateBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BookDTO updatedBook = bookService.updateBook(request, response);
        if (updatedBook != null) {
            response.sendRedirect(request.getContextPath() + "/books/ManageBooks");
        }
    }

    private void handleDeleteBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookIdStr = request.getParameter("bookId");
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdStr);
            boolean deleted = bookService.deleteBook(bookId, request);

            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/books/ManageBooks");
            } else {
                request.setAttribute("error", "Failed to delete book");
                handleManageBooks(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid book ID");
            handleManageBooks(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error occurred while deleting book");
            handleManageBooks(request, response);
        }
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}

