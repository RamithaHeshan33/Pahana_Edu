package org.example.pahana_edu.service;

import org.example.pahana_edu.business.category.dto.CategoryDTO;
import org.example.pahana_edu.business.category.service.CategoryService;
import org.example.pahana_edu.persistance.category.dao.CategoryDAO;
import org.example.pahana_edu.persistance.category.model.CategoryModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    private CategoryService categoryService;
    private FakeCategoryDAO fakeCategoryDAO;
    private CategoryModel categoryModel;
    private CategoryDTO expectedCategoryDTO;

    @BeforeEach
    void setUp() {
        fakeCategoryDAO = new FakeCategoryDAO();
        categoryService = new CategoryService(fakeCategoryDAO);

        categoryModel = new CategoryModel();
        categoryModel.setId(1);
        categoryModel.setCategoryName("Fiction");
        categoryModel.setCategoryDescription("Fictional books");

        expectedCategoryDTO = new CategoryDTO(1, "Fiction", "Fictional books");
    }

    @Test
    void getAllCategories() throws SQLException {
        fakeCategoryDAO.saveCategory(categoryModel);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedCategoryDTO.getCategoryName(), result.get(0).getCategoryName());
        assertEquals(expectedCategoryDTO.getCategoryDescription(), result.get(0).getCategoryDescription());
    }

    @Test
    void getAllCategories_EmptyList() throws SQLException {
        List<CategoryDTO> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCategoryById() throws SQLException {
        fakeCategoryDAO.saveCategory(categoryModel);

        CategoryDTO result = categoryService.getCategoryById(1);

        assertNotNull(result);
        assertEquals(expectedCategoryDTO.getCategoryName(), result.getCategoryName());
        assertEquals(expectedCategoryDTO.getCategoryDescription(), result.getCategoryDescription());
    }

    @Test
    void getCategoryById_NotFound() throws SQLException {
        CategoryDTO result = categoryService.getCategoryById(999);

        assertNull(result);
    }

    @Test
    void saveCategory() throws SQLException {
        fakeCategoryDAO.saveCategory(categoryModel);

        CategoryModel savedCategory = fakeCategoryDAO.getCategoryById(1);
        assertNotNull(savedCategory);
        assertEquals(categoryModel.getCategoryName(), savedCategory.getCategoryName());
        assertEquals(categoryModel.getCategoryDescription(), savedCategory.getCategoryDescription());
    }

    @Test
    void updateCategory() throws SQLException {
        fakeCategoryDAO.saveCategory(categoryModel);
        CategoryModel updatedModel = new CategoryModel();
        updatedModel.setId(1);
        updatedModel.setCategoryName("Non-Fiction");
        updatedModel.setCategoryDescription("Non-fictional books");

        fakeCategoryDAO.updateCategory(updatedModel);

        CategoryModel result = fakeCategoryDAO.getCategoryById(1);
        assertNotNull(result);
        assertEquals("Non-Fiction", result.getCategoryName());
        assertEquals("Non-fictional books", result.getCategoryDescription());
    }

    @Test
    void deleteCategory() throws SQLException {
        fakeCategoryDAO.saveCategory(categoryModel);

        boolean deleted = categoryService.deleteCategory(1, null);

        assertTrue(deleted);
        assertNull(fakeCategoryDAO.getCategoryById(1));
    }

    @Test
    void deleteCategory_NotFound() throws SQLException {
        boolean deleted = categoryService.deleteCategory(999, null);

        assertFalse(deleted);
    }

    @Test
    void getAllCategories_DatabaseError() {
        fakeCategoryDAO.setShouldFail(true);

        SQLException exception = assertThrows(SQLException.class, () -> {
            categoryService.getAllCategories();
        });
        assertEquals("Simulated database error", exception.getMessage());
    }

    @Test
    void getCategoryById_DatabaseError() {
        fakeCategoryDAO.setShouldFail(true);

        SQLException exception = assertThrows(SQLException.class, () -> {
            categoryService.getCategoryById(1);
        });
        assertEquals("Simulated database error", exception.getMessage());
    }

    @Test
    void updateCategory_NotFound() throws SQLException {
        CategoryModel nonExistentCategory = new CategoryModel();
        nonExistentCategory.setId(999);
        nonExistentCategory.setCategoryName("Non-existent");
        nonExistentCategory.setCategoryDescription("Non-existent category");

        SQLException exception = assertThrows(SQLException.class, () -> {
            fakeCategoryDAO.updateCategory(nonExistentCategory);
        });
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void existingCategory_Found() throws SQLException {
        fakeCategoryDAO.saveCategory(categoryModel);

        boolean exists = fakeCategoryDAO.existingCategory("Fiction");

        assertTrue(exists);
    }

    @Test
    void existingCategory_NotFound() throws SQLException {
        boolean exists = fakeCategoryDAO.existingCategory("Non-Fiction");

        assertFalse(exists);
    }

    @Test
    void existingCategoryExcludingId_Found() throws SQLException {
        fakeCategoryDAO.saveCategory(categoryModel);
        CategoryModel anotherCategory = new CategoryModel();
        anotherCategory.setCategoryName("Fiction"); // Same name
        anotherCategory.setCategoryDescription("Another fictional category");
        fakeCategoryDAO.saveCategory(anotherCategory);

        boolean exists = fakeCategoryDAO.existingCategoryExcludingId("Fiction", 1);

        assertTrue(exists);
    }

    @Test
    void existingCategoryExcludingId_NotFound() throws SQLException {
        fakeCategoryDAO.saveCategory(categoryModel);

        boolean exists = fakeCategoryDAO.existingCategoryExcludingId("Fiction", 1);

        assertFalse(exists);
    }

    // Fake CategoryDAO implementation for testing
    private static class FakeCategoryDAO extends CategoryDAO {
        private List<CategoryModel> categories = new ArrayList<>();
        private boolean shouldFail = false;

        void setShouldFail(boolean shouldFail) {
            this.shouldFail = shouldFail;
        }

        void addCategory(CategoryModel category) {
            categories.add(category);
        }

        @Override
        public CategoryModel saveCategory(CategoryModel category) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            category.setId(categories.size() + 1);
            categories.add(category);
            return category;
        }

        @Override
        public CategoryModel getCategoryById(int id) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return categories.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<CategoryModel> getAllCategories() throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return new ArrayList<>(categories);
        }

        @Override
        public CategoryModel updateCategory(CategoryModel category) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId() == category.getId()) {
                    categories.set(i, category);
                    return category;
                }
            }
            throw new SQLException("Category not found");
        }

        @Override
        public boolean deleteCategory(int id) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return categories.removeIf(c -> c.getId() == id);
        }

        @Override
        public boolean existingCategory(String categoryName) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return categories.stream().anyMatch(c -> c.getCategoryName().equals(categoryName));
        }

        @Override
        public boolean existingCategoryExcludingId(String categoryName, int excludeId) throws SQLException {
            if (shouldFail) {
                throw new SQLException("Simulated database error");
            }
            return categories.stream()
                    .anyMatch(c -> c.getCategoryName().equals(categoryName) && c.getId() != excludeId);
        }
    }
}
