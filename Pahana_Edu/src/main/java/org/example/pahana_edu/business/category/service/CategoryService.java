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

public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
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

            // Set success message and clear form data
            request.setAttribute("success", "Category '" + savedCategory.getCategoryName() + "' has been added successfully!");

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
}
