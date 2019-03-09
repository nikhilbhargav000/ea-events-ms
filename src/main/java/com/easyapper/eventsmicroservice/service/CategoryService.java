package com.easyapper.eventsmicroservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyapper.eventsmicroservice.dao.CategoryDao;
import com.easyapper.eventsmicroservice.entity.CategoryEntity;
import com.easyapper.eventsmicroservice.exception.EasyApperDbException;
import com.easyapper.eventsmicroservice.model.CategoryDto;
import com.easyapper.eventsmicroservice.utility.EAConstants;
import com.easyapper.eventsmicroservice.utility.EAUtil;

@Service
public class CategoryService {
	
	@Autowired
	CategoryDao categoryDao;

	public List<CategoryDto> getCatorgies() throws EasyApperDbException{
		return this.getCatorgies(EAConstants.INVALID_PAGINATION_VALUE, 
				EAConstants.INVALID_PAGINATION_VALUE);
	}
	
	public List<CategoryDto> getCatorgies(int page, int total) throws EasyApperDbException{
		List<CategoryDto> categoriesList = new ArrayList<>();
		List<CategoryEntity> categoryEntityList = null;
		if(page != EAConstants.INVALID_PAGINATION_VALUE &&
				total != EAConstants.INVALID_PAGINATION_VALUE) {
			categoryEntityList = categoryDao.getAllCategories(page, total);
		}else {
			categoryEntityList = categoryDao.getAllCategories();
		}
		for(CategoryEntity curCategoryEntity : categoryEntityList) {
			CategoryDto categoryDto = new CategoryDto(curCategoryEntity.get_id(), 
					curCategoryEntity.getName(), curCategoryEntity.getImage_url());
			categoriesList.add(categoryDto);
		}
		return categoriesList;
	}
	
}
