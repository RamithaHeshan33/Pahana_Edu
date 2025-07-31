package org.example.pahana_edu.business.user.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.pahana_edu.persistance.user.dao.UserDAO;
import org.example.pahana_edu.business.user.dto.UserLoginDTO;
import org.example.pahana_edu.business.user.dto.UserRegistrationDTO;
import org.example.pahana_edu.business.user.dto.UserResponseDTO;
import org.example.pahana_edu.business.user.service.UserService;
import org.example.pahana_edu.business.book.dto.BookDTO;
import org.example.pahana_edu.business.book.service.BookService;
import org.example.pahana_edu.business.category.dto.CategoryDTO;
import org.example.pahana_edu.business.category.service.CategoryService;
import org.example.pahana_edu.persistance.category.dao.CategoryDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "authController", urlPatterns = {"/auth/*"})
public class AuthController extends HttpServlet {

    private UserService userService;
    private BookService bookService;
    private CategoryService categoryService;

    @Override
    public void init() throws ServletException {
        userService = new UserService(new UserDAO());
        bookService = new BookService();
        categoryService = new CategoryService(new CategoryDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        switch (pathInfo) {
            case "/login":
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                break;
            case "/register":
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/dashboard":
                handleDashboard(request, response);
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
            case "/login":
                handleLogin(request, response);
                break;
            case "/register":
                handleRegister(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserLoginDTO loginDTO = new UserLoginDTO(username, password);

        try {
            UserResponseDTO user = userService.loginUser(loginDTO);

            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());

            // Redirect to dashboard
            response.sendRedirect(request.getContextPath() + "/auth/dashboard");

        } catch (SQLException e) {
            request.setAttribute("error", "Database error occurred. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO(
                username, email, password, confirmPassword, firstName, lastName
        );

        try {
            UserResponseDTO user = userService.registerUser(registrationDTO);

            // Set success message
            request.setAttribute("success", "Registration successful! Please login.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "Database error occurred. Please try again.");
            request.setAttribute("registrationData", registrationDTO);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("registrationData", registrationDTO);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    private void handleDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        } catch (SQLException e) {
            request.setAttribute("error", "Error loading dashboard data: " + e.getMessage());
        }

        request.getRequestDispatcher("/Admin/dashboard.jsp").forward(request, response);
    }
}