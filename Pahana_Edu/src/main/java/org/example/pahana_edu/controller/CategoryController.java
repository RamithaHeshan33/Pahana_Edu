package org.example.pahana_edu.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.pahana_edu.dao.CategoryDAO;
import org.example.pahana_edu.dto.CategoryDTO;
import org.example.pahana_edu.service.CategoryService;

import java.io.IOException;

@WebServlet(name = "categoryController", urlPatterns = {"/categories/*"})
public class CategoryController extends HttpServlet {
    private final CategoryService categoryService;

    public CategoryController() {
        this.categoryService = new CategoryService(new CategoryDAO()); // Simple instantiation; consider dependency injection
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
            case "/ManageCategories":
                request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
                break;
            case "/saveCategory":
                request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
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
            case "/saveCategory":
                handleSaveCategory(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleSaveCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Extract parameters
        String categoryName = request.getParameter("categoryName");
        String categoryDescription = request.getParameter("categoryDescription");

        // Log parameters for debugging
        System.out.println("categoryName: " + categoryName);
        System.out.println("categoryDescription: " + categoryDescription);

        // Create DTO
        CategoryDTO categoryDTO = new CategoryDTO(categoryName, categoryDescription);

        // Validate DTO
        if (!categoryDTO.isValid()) {
            String errorMessage = "Invalid category data: Category name is required";
            System.out.println("Validation failed: " + errorMessage);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
            return;
        }

        // Save via service
        CategoryDTO savedCategory = categoryService.saveCategory(request, response);
        if (savedCategory != null) {
            response.sendRedirect(request.getContextPath() + "/categories/ManageCategories");
        }
    }
}
