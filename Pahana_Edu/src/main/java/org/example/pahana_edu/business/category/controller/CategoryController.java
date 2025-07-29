package org.example.pahana_edu.business.category.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.pahana_edu.persistance.category.dao.CategoryDAO;
import org.example.pahana_edu.business.category.dto.CategoryDTO;
import org.example.pahana_edu.business.category.service.CategoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "categoryController", urlPatterns = {"/categories/*"})
public class CategoryController extends HttpServlet {
    private final CategoryService categoryService;

    public CategoryController() {
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
            case "/ManageCategories":
                handleManageCategories(request, response);
                break;
            case "/getCategory":
                handleGetCategory(request, response);
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
            case "/saveCategory":
                handleSaveCategory(request, response);
                break;
            case "/updateCategory":
                handleUpdateCategory(request, response);
                break;
            case "/deleteCategory":
                handleDeleteCategory(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleManageCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error loading categories: " + e.getMessage());
            request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
        }
    }

    private void handleGetCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String categoryIdStr = request.getParameter("id");
        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category ID is required");
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            CategoryDTO category = categoryService.getCategoryById(categoryId);

            if (category == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found");
                return;
            }

            // Return JSON response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.format(
                    "{\"id\":%d,\"name\":\"%s\",\"description\":\"%s\"}",
                    category.getId(),
                    category.getCategoryName().replace("\"", "\\\""),
                    category.getCategoryDescription() != null ? category.getCategoryDescription().replace("\"", "\\\"") : ""
            ));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void handleUpdateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDTO updatedCategory = categoryService.updateCategory(request, response);
        if (updatedCategory != null) {
            response.sendRedirect(request.getContextPath() + "/categories/ManageCategories");
        }
    }

    private void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String categoryIdStr = request.getParameter("categoryId");
        if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category ID is required");
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryIdStr);
            boolean deleted = categoryService.deleteCategory(categoryId, request);

            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/categories/ManageCategories");
            } else {
                request.setAttribute("error", "Failed to delete category");
                handleManageCategories(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid category ID");
            handleManageCategories(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error occurred while deleting category");
            handleManageCategories(request, response);
        }
    }
    private void handleSaveCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Extract parameters
        String categoryName = request.getParameter("categoryName");
        String categoryDescription = request.getParameter("categoryDescription");

        // Create DTO
        CategoryDTO categoryDTO = new CategoryDTO(categoryName, categoryDescription);

        // Validate DTO
        if (!categoryDTO.isValid()) {
            request.setAttribute("error", "Invalid category data: Category name is required");
            request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
            return;
        }

        // Save via service
        CategoryDTO savedCategory = categoryService.saveCategory(request, response);
        if (savedCategory != null) {
            response.sendRedirect(request.getContextPath() + "/categories/ManageCategories");
        }
    }
}
