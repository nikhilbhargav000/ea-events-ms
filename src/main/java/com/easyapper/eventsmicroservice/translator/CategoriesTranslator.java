package com.easyapper.eventsmicroservice.translator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.easyapper.eventsmicroservice.entity.CategoryEntity;
import com.easyapper.eventsmicroservice.model.CategoryDto;

@Component
public class CategoriesTranslator {

	public List<CategoryDto> getCategoriesList(List<CategoryEntity> categoryEntityList) {
		List<CategoryDto> categoriesList = new ArrayList<>();
		for(CategoryEntity curCategoryEntity : categoryEntityList) {
			CategoryDto categoryDto = this.getCategoryDto(curCategoryEntity);
			categoriesList.add(categoryDto);
		}
		return categoriesList;
	}

	public CategoryDto getCategoryDto(CategoryEntity categoryEntity) {
		return new CategoryDto(categoryEntity.get_id(), 
				categoryEntity.getName(), categoryEntity.getImage_url(), 
				categoryEntity.getRegex_file_name());
	}
	public CategoryEntity getCategoryEntity(CategoryDto categoryDto) {
		return new CategoryEntity(categoryDto.getId(), 
				categoryDto.getName(), categoryDto.getImage_url(), 
				categoryDto.getRegexFileName());
	}
}

