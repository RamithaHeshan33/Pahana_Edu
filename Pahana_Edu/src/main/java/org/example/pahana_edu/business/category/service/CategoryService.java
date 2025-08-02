package org.example.pahana_edu.business.category.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.pahana_edu.persistance.category.dao.CategoryDAO;
import org.example.pahana_edu.business.category.dto.CategoryDTO;
import org.example.pahana_edu.business.category.mapper.CategoryMapper;
import org.example.pahana_edu.persistance.category.model.CategoryModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<CategoryDTO> getAllCategories() throws SQLException {
        List<CategoryModel> categories = categoryDAO.getAllCategories();
        return categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(int id) throws SQLException {
        CategoryModel category = categoryDAO.getCategoryById(id);
        return category != null ? CategoryMapper.toDTO(category) : null;
    }

    public CategoryDTO saveCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String categoryDescription = null;
        String categoryName = null;
        try {
            // Extract parameters from request
            categoryName = request.getParameter("categoryName");
            categoryDescription = request.getParameter("categoryDescription");

            // Create and validate DTO first
            CategoryDTO categoryDTO = new CategoryDTO(categoryName, categoryDescription);
            if (!categoryDTO.isValid()) {
                request.setAttribute("error", "Category name is required");
                request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
                return null;
            }

            // Check if category already exists
            if (categoryDAO.existingCategory(categoryName)) {
                request.setAttribute("error", "Category with name '" + categoryName + "' already exists");
                request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
                return null;
            }

            // Convert DTO to Model
            CategoryModel category = CategoryMapper.toEntity(categoryDTO);

            // Save to database
            CategoryModel savedCategory = categoryDAO.saveCategory(category);

            // Set success message in session
            request.getSession().setAttribute("success", "Category '" + savedCategory.getCategoryName() + "' has been added successfully!");

            // Convert saved Model back to DTO
            return new CategoryDTO(savedCategory.getCategoryName(), savedCategory.getCategoryDescription());
        } catch (SQLException e) {
            // Preserve form data on error
            request.setAttribute("categoryName", categoryName);
            request.setAttribute("categoryDescription", categoryDescription);
            request.setAttribute("error", "Database error occurred while saving category. Please try again.");
            request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
            return null;
        }
    }

    public CategoryDTO updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Extract parameters from request
            String categoryIdStr = request.getParameter("categoryId");
            String categoryName = request.getParameter("categoryName");
            String categoryDescription = request.getParameter("categoryDescription");

            if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Category ID is required");
                request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
                return null;
            }

            int categoryId = Integer.parseInt(categoryIdStr);

            // Create and validate DTO
            CategoryDTO categoryDTO = new CategoryDTO(categoryId, categoryName, categoryDescription);
            if (!categoryDTO.isValid()) {
                request.setAttribute("error", "Category name is required");
                request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
                return null;
            }

            // Check if category name already exists (excluding current category)
            if (categoryDAO.existingCategoryExcludingId(categoryName, categoryId)) {
                request.setAttribute("error", "Category with name '" + categoryName + "' already exists");
                request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
                return null;
            }

            // Convert DTO to Model
            CategoryModel category = CategoryMapper.toEntity(categoryDTO);
            category.setId(categoryId);

            // Update in database
            CategoryModel updatedCategory = categoryDAO.updateCategory(category);

            // Set success message in session
            request.getSession().setAttribute("success", "Category '" + updatedCategory.getCategoryName() + "' has been updated successfully!");

            // Convert updated Model back to DTO
            return CategoryMapper.toDTO(updatedCategory);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid category ID");
            request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
            return null;
        } catch (SQLException e) {
            request.setAttribute("error", "Database error occurred while updating category. Please try again.");
            request.getRequestDispatcher("/Admin/ManageCategories.jsp").forward(request, response);
            return null;
        }
    }

    public boolean deleteCategory(int id, HttpServletRequest request) throws SQLException {
        boolean deleted = categoryDAO.deleteCategory(id);
        if (deleted && request != null) {
            request.getSession().setAttribute("success", "Category has been deleted successfully!");
        }
        return deleted;
    }
}
