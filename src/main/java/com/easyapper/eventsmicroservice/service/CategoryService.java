package com.easyapper.eventsmicroservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyapper.eventsmicroservice.dao.CategoryDao;
import com.easyapper.eventsmicroservice.entity.CategoryEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.model.CategoryDto;

@Service
public class CategoryService {
	
	@Autowired
	CategoryDao categoryDao;
	
	public List<CategoryDto> getCatorgies() throws EasyApperDbException{
		
		List<CategoryDto> categoriesList = new ArrayList<>();
		List<CategoryEntity> categoryEntityList =  categoryDao.getAllCategories();
		
		for(CategoryEntity curCategoryEntity : categoryEntityList) {
			CategoryDto categoryDto = new CategoryDto(curCategoryEntity.get_id(), 
					curCategoryEntity.getName(), curCategoryEntity.getImage_url());
			categoriesList.add(categoryDto);
		}
		
		return categoriesList;
	}
	
}
