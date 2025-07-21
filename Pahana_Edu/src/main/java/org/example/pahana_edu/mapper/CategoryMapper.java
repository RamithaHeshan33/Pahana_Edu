package org.example.pahana_edu.mapper;

import org.example.pahana_edu.dto.CategoryDTO;
import org.example.pahana_edu.model.CategoryModel;

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
