package org.example.pahana_edu.business.dashboard.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.pahana_edu.business.book.dto.BookDTO;
import org.example.pahana_edu.business.book.service.BookService;
import org.example.pahana_edu.business.category.dto.CategoryDTO;
import org.example.pahana_edu.business.category.service.CategoryService;
import org.example.pahana_edu.persistance.category.dao.CategoryDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "dashboardController", urlPatterns = {"/dashboard/*"})
public class DashboardController extends HttpServlet {
    private final BookService bookService;
    private final CategoryService categoryService;

    public DashboardController() {
        this.bookService = new BookService();
        this.categoryService = new CategoryService(new CategoryDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        try {
            // Get all books for display
            List<BookDTO> books = bookService.getAllBooks();
            request.setAttribute("books", books);

            // Get all categories for search filter
            List<CategoryDTO> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);

            // Forward to dashboard
            request.getRequestDispatcher("/Admin/dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "Error loading dashboard data: " + e.getMessage());
            request.getRequestDispatcher("/Admin/dashboard.jsp").forward(request, response);
        }
    }
}