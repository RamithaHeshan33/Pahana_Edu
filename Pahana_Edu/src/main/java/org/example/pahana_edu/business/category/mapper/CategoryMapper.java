package org.example.pahana_edu.business.category.mapper;

import org.example.pahana_edu.business.category.dto.CategoryDTO;
import org.example.pahana_edu.persistance.category.model.CategoryModel;

public class CategoryMapper {
    public static CategoryModel toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        return new CategoryModel(categoryDTO.getCategoryName(), categoryDTO.getCategoryDescription());
    }

    public static CategoryDTO toDTO(CategoryModel categoryModel) {
        if (categoryModel == null) {
            return null;
        }
        return new CategoryDTO(categoryModel.getId(), categoryModel.getCategoryName(), categoryModel.getCategoryDescription());
    }
}
